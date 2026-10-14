package service

type UserService struct {
	userRepository UserRepository
}

type UserRepository interface {

}

func NewUserService(userRepository UserRepository) *UserService {
	return &UserService{userRepository: userRepository}
}
