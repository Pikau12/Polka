package service

import (
	"context"
	"fmt"

	"github.com/lib/pq"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/model"
)

type SearchGameResult struct {
	Games      []domain.GameSearchInfo
	NextOffset int32
	HasNext    bool
}

type CreateGameResult struct {
	GameID int64
	Name   string
}

type GetGameResult struct {
	ServerID              int64
	BggID                 *int64
	Name                  string
	Description           *string
	YearPublished         int32
	BggRating             *float64
	PolkaRating           *float64
	BestCountPlayers      []int32
	AvailableCountPlayers []int32
	MinPlayTimeMinutes    *int32
	MaxPlayTimeMinutes    *int32
	MinAge                *int32
	Weight                *float64
}

type GameService struct {
	gameRepository GameRepository
	bggClient      GameClient
}

type GameRepository interface {
	SearchGameByFilter(c context.Context, gameFilter *domain.GameFilter) ([]domain.GameSearchInfo, error)
	FindGamesByBggID(ctx context.Context, ids []int64) ([]int64, error)
	GetCountGamesByName(ctx context.Context, name string) (int64, error)
	CreateGame(ctx context.Context, game model.Game) (*model.Game, error)
	GetGameByID(ctx context.Context, gameID int64) (*model.Game, error)
}

type GameClient interface {
	Search(ctx context.Context, name string) ([]domain.BggGameSearch, error)
	Thing(ctx context.Context, bggIDs []int64) ([]domain.BggGameThing, error)
}

func NewGameService(gameRepository GameRepository, bggClient GameClient) *GameService {
	return &GameService{gameRepository: gameRepository, bggClient: bggClient}
}

func (s *GameService) SearchGames(ctx context.Context, request dto.SearchGameRequest) (*SearchGameResult, error) {
	res := &SearchGameResult{}

	filter := searchGameRequestToGameFilter(request)

	normalizeFilter(filter)

	if err := validateFilter(filter); err != nil {
		return nil, fmt.Errorf("invalid filter: %w", err)
	}

	games, err := s.gameRepository.SearchGameByFilter(ctx, filter)
	if err != nil {
		return nil, err
	}

	localCount, err := s.gameRepository.GetCountGamesByName(ctx, filter.Name)
	if err != nil {
		return nil, err
	}

	if localCount > int64(request.Offset+request.Limit) {
		res.HasNext = true
	} else {
		needCount := int(request.Limit) - len(games)
		bggOffset := int(request.Offset) - int(localCount)
		if bggOffset < 0 {
			bggOffset = 0
		}

		bggGames, hasNext, err := s.searchGamesWithBgg(ctx, request.Name, bggOffset, needCount)
		if err != nil {
			return nil, fmt.Errorf("search bgg games: %w", err)
		}

		games = append(games, bggGames...)
		res.HasNext = hasNext
	}

	res.Games = games
	res.NextOffset = request.Offset + int32(len(games))

	return res, nil
}

func (s *GameService) GetGame(ctx context.Context, request dto.GetGamesRequest) ([]GetGameResult, error) {
	if len(request.IDs) == 0 {
		return nil, fmt.Errorf("no IDs provided")
	}

	toResult := func(game *model.Game) GetGameResult {
		var yearPublished int32

		if game.YearPublished != nil {
			yearPublished = *game.YearPublished
		}

		return GetGameResult{
			ServerID:              game.ID,
			BggID:                 game.BggID,
			Name:                  game.Name,
			Description:           game.Description,
			YearPublished:         yearPublished,
			BggRating:             game.BggRating,
			PolkaRating:           game.PolkaRating,
			BestCountPlayers:      game.BestCountPlayers,
			AvailableCountPlayers: game.AvailableCountPlayers,
			MinPlayTimeMinutes:    game.MinPlayTimeMinutes,
			MaxPlayTimeMinutes:    game.MaxPlayTimeMinutes,
			MinAge:                game.MinAge,
			Weight:                game.Weight,
		}
	}

	localGames := make(map[int64]GetGameResult)

	bggGames := make(map[int64]GetGameResult)

	bggIDs := make([]int64, 0, len(request.IDs))

	seenBggIDs := make(map[int64]struct{})

	for _, gameInfo := range request.IDs {
		if gameInfo.ServerID != 0 && gameInfo.BggID != 0 {
			return nil, fmt.Errorf("game must contain either server_id or bgg_id, not both")
		}

		if gameInfo.ServerID == 0 && gameInfo.BggID == 0 {
			return nil, fmt.Errorf("game must contain server_id or bgg_id")
		}

		if gameInfo.ServerID != 0 {
			if _, exists := localGames[gameInfo.ServerID]; exists {
				continue
			}

			game, err := s.gameRepository.GetGameByID(ctx, gameInfo.ServerID)
			if err != nil {
				return nil, fmt.Errorf("get local game %d: %w", gameInfo.ServerID, err)
			}

			localGames[gameInfo.ServerID] = toResult(game)

			continue
		}

		if _, exists := seenBggIDs[gameInfo.BggID]; exists {
			continue
		}

		seenBggIDs[gameInfo.BggID] = struct{}{}
		bggIDs = append(bggIDs, gameInfo.BggID)
	}

	if len(bggIDs) > 0 {
		things, err := s.bggClient.Thing(ctx, bggIDs)
		if err != nil {
			return nil, fmt.Errorf("get games from bgg: %w", err)
		}

		for _, game := range things {
			availableCountPlayers := make([]int32, 0)

			if game.MinPlayers > 0 && game.MaxPlayers >= game.MinPlayers {
				availableCountPlayers = make([]int32, 0, game.MaxPlayers-game.MinPlayers+1)

				for players := game.MinPlayers; players <= game.MaxPlayers; players++ {
					availableCountPlayers = append(availableCountPlayers, players)
				}
			}

			// TODO: сохранить Image/Thumbnail в Garage,

			modelGame := model.Game{
				BggID:                 &game.BggID,
				Name:                  game.Name,
				Description:           &game.Description,
				YearPublished:         &game.YearPublished,
				AvailableCountPlayers: pq.Int32Array(availableCountPlayers),
				MinPlayTimeMinutes:    &game.MinPlayTime,
				MaxPlayTimeMinutes:    &game.MaxPlayTime,
				MinAge:                &game.MinAge,
			}

			savedGame, err := s.gameRepository.CreateGame(ctx, modelGame)
			if err != nil {
				return nil, fmt.Errorf("save bgg game %d: %w", game.BggID, err)
			}

			bggGames[game.BggID] = toResult(savedGame)
		}

		for _, bggID := range bggIDs {
			if _, exists := bggGames[bggID]; !exists {
				return nil, fmt.Errorf("bgg game %d was not returned", bggID)
			}
		}
	}

	result := make([]GetGameResult, 0, len(request.IDs))

	for _, gameInfo := range request.IDs {
		if gameInfo.ServerID != 0 {
			game, exists := localGames[gameInfo.ServerID]
			if !exists {
				return nil, fmt.Errorf("local game %d not found in result", gameInfo.ServerID)
			}

			result = append(result, game)
			continue
		}

		game, exists := bggGames[gameInfo.BggID]
		if !exists {
			return nil, fmt.Errorf("bgg game %d not found in result", gameInfo.BggID)
		}

		result = append(result, game)
	}

	return result, nil
}

func (s *GameService) CreateGame(ctx context.Context, name string) (*CreateGameResult, error) {
	game, err := s.gameRepository.CreateGame(ctx, model.Game{Name: name})
	if err != nil {
		return nil, fmt.Errorf("create game: %w", err)
	}

	return &CreateGameResult{
		GameID: game.ID,
		Name:   game.Name,
	}, nil
}

func (s *GameService) searchGamesWithBgg(ctx context.Context, name string, bggOffset int, needCount int) ([]domain.GameSearchInfo, bool, error) {
	games := make([]domain.GameSearchInfo, 0, needCount)

	bggGames, err := s.bggClient.Search(ctx, name)
	if err != nil {
		return nil, false, fmt.Errorf("search game: %w", err)
	}

	gamesBggIDs := make([]int64, 0, len(bggGames))

	for _, item := range bggGames {
		gamesBggIDs = append(gamesBggIDs, item.BggID)
	}

	usedBggIDs, err := s.gameRepository.FindGamesByBggID(ctx, gamesBggIDs)
	if err != nil {
		return nil, false, fmt.Errorf("find bgg: %w", err)
	}

	usedSet := make(map[int64]bool, len(usedBggIDs))
	for _, id := range usedBggIDs {
		usedSet[id] = true
	}

	skip := bggOffset

	for _, game := range bggGames {
		if usedSet[game.BggID] {
			continue
		}

		if skip > 0 {
			skip--
			continue
		}

		if len(games) == needCount {
			return games, true, nil
		}

		games = append(games, domain.GameSearchInfo{
			BggID:         game.BggID,
			Name:          game.Name,
			YearPublished: &game.YearPublished,
		})
	}

	return games, false, nil
}

func searchGameRequestToGameFilter(request dto.SearchGameRequest) *domain.GameFilter {
	return &domain.GameFilter{
		Name: request.Name,

		MinPlayTimeMinutes: request.MinPlayTimeMinutes,
		MaxPlayTimeMinutes: request.MaxPlayTimeMinutes,

		MinAge: request.MinAge,

		AvailableCountPlayers: request.AvailableCountPlayers,

		MinWeight: request.MinWeight,
		MaxWeight: request.MaxWeight,

		MinPolkaRating: request.MinPolkaRating,
		MinBggRating:   request.MinBggRating,

		Mechanics:  request.Mechanics,
		Categories: request.Categories,
		Publishers: request.Publishers,
		Designers:  request.Designers,

		Offset: request.Offset,
		Limit:  request.Limit,
	}
}

func validateFilter(filter *domain.GameFilter) error {
	if filter.MinPlayTimeMinutes != nil && *filter.MinPlayTimeMinutes < 0 {
		return fmt.Errorf("min_play_time_minutes must be greater than or equal to 0")
	}

	if filter.MaxPlayTimeMinutes != nil && *filter.MaxPlayTimeMinutes < 0 {
		return fmt.Errorf("max_play_time_minutes must be greater than or equal to 0")
	}

	if filter.MinAge != nil && *filter.MinAge < 0 {
		return fmt.Errorf("min_age must be greater than or equal to 0")
	}

	if filter.AvailableCountPlayers != nil && len(filter.AvailableCountPlayers) > 0 {
		for _, count := range filter.AvailableCountPlayers {
			if count <= 0 {
				return fmt.Errorf("available_count_players must be greater than or equal to 0")
			}
		}
	}

	if filter.MinWeight != nil && *filter.MinWeight < 0 {
		return fmt.Errorf("min_weight must be greater than or equal to 0")
	}

	if filter.MaxWeight != nil && *filter.MaxWeight < 0 {
		return fmt.Errorf("max_weight must be greater than or equal to 0")
	}

	if filter.MinPolkaRating != nil && *filter.MinPolkaRating < 0 {
		return fmt.Errorf("min_polka_rating must be greater than or equal to 0")
	}

	if filter.MinBggRating != nil && *filter.MinBggRating < 0 {
		return fmt.Errorf("min_bgg_rating must be greater than or equal to 0")
	}

	if filter.Offset < 0 {
		return fmt.Errorf("invalid offset")
	}

	if filter.Limit < 0 && filter.Limit > 100 {
		return fmt.Errorf("invalid limit")
	}

	return nil
}

func normalizeFilter(filter *domain.GameFilter) *domain.GameFilter {
	if filter.Mechanics != nil && len(filter.Mechanics) > 0 {
		filter.Mechanics = deleteDuplicates(filter.Mechanics)
	}

	if filter.Categories != nil && len(filter.Categories) > 0 {
		filter.Categories = deleteDuplicates(filter.Categories)
	}

	if filter.Designers != nil && len(filter.Designers) > 0 {
		filter.Designers = deleteDuplicates(filter.Designers)
	}

	if filter.Publishers != nil && len(filter.Publishers) > 0 {
		filter.Publishers = deleteDuplicates(filter.Publishers)
	}

	return filter
}

func deleteDuplicates(slice []string) []string {
	set := make(map[string]bool, len(slice))
	res := make([]string, 0, len(slice))

	for _, v := range slice {
		if set[v] {
			continue
		}

		set[v] = true
		res = append(res, v)
	}

	return res
}
