package service

type SessionService struct {
	sessionRepository SessionRepository
}

type SessionRepository interface {

}

func NewSessionService(sessionRepository SessionRepository) *SessionService {
	return &SessionService{sessionRepository: sessionRepository}
}
