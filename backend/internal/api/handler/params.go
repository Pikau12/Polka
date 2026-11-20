package handler

import (
	"fmt"
	"strconv"

	"github.com/gin-gonic/gin"
)

func getUserIDFromContext(c *gin.Context) (*int64, error) {
	value, exists := c.Get("user_id")
	if !exists {
		return nil, fmt.Errorf("unauthorized")
	}

	id, ok := value.(int64)
	if !ok {
		return nil, fmt.Errorf("invalid type user id")
	}

	return &id, nil
}

func parsePositiveInt64Param(c *gin.Context, name string) (int64, error) {
	id, err := strconv.ParseInt(c.Param(name), 10, 64)
	if err != nil || id <= 0 {
		return 0, fmt.Errorf("invalid %s", name)
	}
	return id, nil
}
