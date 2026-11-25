package repository

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/model"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

var (
	ErrSessionNotFound           = errors.New("session not found")
	ErrSessionConflict           = errors.New("session has changed")
	ErrSessionParticipantInvalid = errors.New("session participant invalid")
)

type SessionRepository struct {
	db *gorm.DB
}

func NewSessionRepository(db *gorm.DB) *SessionRepository {
	return &SessionRepository{db: db}
}

// TODO: When friendship will be realized. Check creator is friend of participant

func (r *SessionRepository) Create(ctx context.Context, session *domain.Session) (*domain.Session, error) {
	var created *domain.Session

	err := r.db.Transaction(func(tx *gorm.DB) error {
		dbSession, err := sessionToModel(session)
		if err != nil {
			return fmt.Errorf("create session model: %w", err)
		}

		if err := tx.
			WithContext(ctx).
			Create(dbSession).
			Error; err != nil {
			if errors.Is(err, gorm.ErrForeignKeyViolated) {
				return fmt.Errorf("create session reference conflict: %w", ErrSessionConflict)
			}

			return fmt.Errorf("create session: %w", err)
		}

		participants := make([]domain.SessionParticipant, 0, len(session.Participants))

		for _, participant := range session.Participants {
			mdlParticipant, err := sessionParticipantToModel(&participant, session.ID)
			if err != nil {
				return fmt.Errorf("create participant model: %w", err)
			}

			if err := tx.
				WithContext(ctx).
				Create(mdlParticipant).
				Error; err != nil {
				return fmt.Errorf("create participant model: %w", err)
			}

			participants = append(participants, *sessionParticipantToDomain(mdlParticipant))
		}

		created = sessionToDomain(dbSession)
		created.Participants = participants

		return nil
	})

	return created, err
}

func (r *SessionRepository) GetAll(ctx context.Context, userID int64) ([]domain.Session, error) {
	sessions := make([]model.Session, 0)

	err := r.db.
		WithContext(ctx).
		Preload("Participants", func(db *gorm.DB) *gorm.DB {
			return db.Order("session_participants.id ASC")
		}).
		Where(`sessions.creator_id = ? OR EXISTS (
			SELECT 1 FROM session_participants
			WHERE session_participants.session_id = sessions.id
			AND session_participants.user_id = ?
		)`, userID, userID).
		Order("sessions.created_at DESC").
		Order("sessions.id DESC").
		Find(&sessions).
		Error
	if err != nil {
		return nil, fmt.Errorf("get user sessions: %w", err)
	}

	result := make([]domain.Session, 0, len(sessions))
	for i := range sessions {
		result = append(result, *sessionToDomain(&sessions[i]))
	}
	return result, nil
}

func (r *SessionRepository) GetByID(ctx context.Context, id int64) (*domain.Session, error) {
	session := &model.Session{}
	if err := r.db.
		WithContext(ctx).
		Preload("Participants", func(db *gorm.DB) *gorm.DB {
			return db.Order("session_participants.id ASC")
		}).
		Where("id = ?", id).
		Take(session).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, ErrSessionNotFound
		}
		return nil, fmt.Errorf("get session by id: %w", err)
	}

	return sessionToDomain(session), nil
}

func (r *SessionRepository) IsParticipant(ctx context.Context, sessionID, userID int64) (bool, error) {
	var count int64
	err := r.db.
		WithContext(ctx).
		Table("session_participants").
		Where("session_id = ? AND user_id = ?", sessionID, userID).
		Count(&count).
		Error
	if err != nil {
		return false, fmt.Errorf("check session participant: %w", err)
	}

	return count > 0, nil
}

func (r *SessionRepository) Update(
	ctx context.Context,
	session *domain.Session,
) (*domain.Session, error) {
	if session == nil {
		return nil, fmt.Errorf("update session: session is nil")
	}

	updatedAt := time.Now().UTC().Truncate(time.Microsecond)
	if !updatedAt.After(session.UpdatedAt) {
		updatedAt = session.UpdatedAt.Add(time.Microsecond)
	}

	var updated *domain.Session

	err := r.db.WithContext(ctx).Transaction(func(tx *gorm.DB) error {
		result := tx.
			Table("sessions").
			Where(
				"id = ? AND creator_id = ? AND updated_at = ?",
				session.ID,
				session.CreatorID,
				session.UpdatedAt,
			).
			Updates(map[string]any{
				"game_id":     session.GameID,
				"note":        session.Note,
				"place":       session.Place,
				"status":      model.SessionStatus(session.Status),
				"started_at":  session.StartedAt,
				"finished_at": session.FinishedAt,
				"updated_at":  updatedAt,
			})

		if result.Error != nil {
			return fmt.Errorf("update session fields: %w", result.Error)
		}
		if result.RowsAffected != 1 {
			return ErrSessionConflict
		}

		if session.Participants != nil {
			if err := syncSessionParticipants(
				tx,
				session.ID,
				session.Participants,
			); err != nil {
				return err
			}
		}

		var dbSession model.Session
		if err := tx.
			Where("id = ?", session.ID).
			Take(&dbSession).Error; err != nil {
			return fmt.Errorf("read updated session: %w", err)
		}

		var dbParticipants []model.SessionParticipant
		if err := tx.
			Where("session_id = ?", session.ID).
			Order("id ASC").
			Find(&dbParticipants).Error; err != nil {
			return fmt.Errorf("read updated participants: %w", err)
		}

		updated = sessionToDomain(&dbSession)
		updated.Participants = make(
			[]domain.SessionParticipant,
			0,
			len(dbParticipants),
		)

		for i := range dbParticipants {
			updated.Participants = append(
				updated.Participants,
				*sessionParticipantToDomain(&dbParticipants[i]),
			)
		}

		return nil
	})
	if err != nil {
		if errors.Is(err, gorm.ErrForeignKeyViolated) {
			return nil, fmt.Errorf(
				"update session reference conflict: %w",
				ErrSessionConflict,
			)
		}
		if errors.Is(err, gorm.ErrDuplicatedKey) {
			return nil, fmt.Errorf(
				"duplicate participant: %w",
				ErrSessionParticipantInvalid,
			)
		}

		return nil, fmt.Errorf("update session: %w", err)
	}

	return updated, nil
}

func (r *SessionRepository) Delete(ctx context.Context, id, creatorID int64) error {
	result := r.db.
		WithContext(ctx).
		Where("id = ? AND creator_id = ?", id, creatorID).
		Delete(&model.Session{})
	if result.Error != nil {
		return fmt.Errorf("delete session: %w", result.Error)
	}
	if result.RowsAffected == 0 {
		return ErrSessionNotFound
	}

	return nil
}

func syncSessionParticipants(
	tx *gorm.DB,
	sessionID int64,
	participants []domain.SessionParticipant,
) error {
	ids := make([]int64, 0, len(participants))
	seen := make(map[int64]struct{}, len(participants))
	models := make([]model.SessionParticipant, 0, len(participants))

	for i := range participants {
		participant := &participants[i]

		mdl, err := sessionParticipantToModel(participant, sessionID)
		if err != nil {
			return fmt.Errorf("convert participant: %w", err)
		}

		if participant.ID != nil {
			id := *participant.ID

			if id <= 0 {
				return ErrSessionParticipantInvalid
			}
			if _, exists := seen[id]; exists {
				return ErrSessionParticipantInvalid
			}

			seen[id] = struct{}{}
			ids = append(ids, id)
			mdl.ID = id
		}

		models = append(models, *mdl)
	}

	deletion := tx.Where("session_id = ?", sessionID)
	if len(ids) > 0 {
		deletion = deletion.Where("id NOT IN ?", ids)
	}

	if err := deletion.
		Delete(&model.SessionParticipant{}).Error; err != nil {
		return fmt.Errorf("delete removed participants: %w", err)
	}

	for i := range models {
		participant := &models[i]
		if participant.ID == 0 {
			continue
		}

		result := tx.
			Model(&model.SessionParticipant{}).
			Where(
				"id = ? AND session_id = ?",
				participant.ID,
				sessionID,
			).
			Updates(map[string]any{
				"user_id":    participant.UserID,
				"guest_name": participant.GuestName,
				"score":      participant.Score,
				"is_winner":  participant.IsWinner,
			})

		if result.Error != nil {
			return fmt.Errorf("update participant: %w", result.Error)
		}
		if result.RowsAffected != 1 {
			return ErrSessionParticipantInvalid
		}
	}

	for i := range models {
		if models[i].ID != 0 {
			continue
		}

		if err := tx.
			Omit(clause.Associations).
			Create(&models[i]).Error; err != nil {
			return fmt.Errorf("insert participant: %w", err)
		}
	}

	return nil
}

func sessionToModel(session *domain.Session) (*model.Session, error) {
	return &model.Session{
		ID:         session.ID,
		GameID:     session.GameID,
		CreatorID:  session.CreatorID,
		Note:       session.Note,
		Place:      session.Place,
		Status:     model.SessionStatus(session.Status),
		StartedAt:  session.StartedAt,
		FinishedAt: session.FinishedAt,
		CreatedAt:  session.CreatedAt,
		UpdatedAt:  session.UpdatedAt,
	}, nil
}

func sessionToDomain(session *model.Session) *domain.Session {
	participants := make([]domain.SessionParticipant, 0, len(session.Participants))
	for i := range session.Participants {
		participants = append(participants, *sessionParticipantToDomain(&session.Participants[i]))
	}

	return &domain.Session{
		ID:           session.ID,
		GameID:       session.GameID,
		CreatorID:    session.CreatorID,
		Note:         session.Note,
		Place:        session.Place,
		Status:       string(session.Status),
		StartedAt:    session.StartedAt,
		FinishedAt:   session.FinishedAt,
		CreatedAt:    session.CreatedAt,
		UpdatedAt:    session.UpdatedAt,
		Participants: participants,
	}
}

func sessionParticipantToModel(participant *domain.SessionParticipant, sessionID int64) (*model.SessionParticipant, error) {
	if (participant.UserID == nil) == (participant.GuestName == nil) {
		return nil, ErrSessionParticipantInvalid
	}

	return &model.SessionParticipant{
		SessionID: sessionID,
		UserID:    participant.UserID,
		GuestName: participant.GuestName,
		Score:     participant.Score,
		IsWinner:  &participant.IsWinner,
	}, nil
}

func sessionParticipantToDomain(sessionParticipant *model.SessionParticipant) *domain.SessionParticipant {
	return &domain.SessionParticipant{
		ID:        &sessionParticipant.ID,
		UserID:    sessionParticipant.UserID,
		GuestName: sessionParticipant.GuestName,
		Score:     sessionParticipant.Score,
		IsWinner:  *sessionParticipant.IsWinner,
	}
}
