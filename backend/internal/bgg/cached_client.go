package bgg

import (
	"context"
	"fmt"
	"strings"
	"sync"
	"time"

	"github.com/polka/backend/internal/domain"
)

type Searcher interface {
	Search(ctx context.Context, name string) ([]domain.BggGameSearch, error)
	Thing(ctx context.Context, bggIDs []int64) ([]domain.BggGameThing, error)
}

type CachedClient struct {
	client Searcher

	mtx   sync.RWMutex
	cache map[string]searchGameCacheEntry
	ttl   time.Duration

	stop     chan struct{}
	stopOnce sync.Once
}

type searchGameCacheEntry struct {
	games     []domain.BggGameSearch
	expiresAt time.Time
}

func NewCachedClient(client Searcher, ttl time.Duration) *CachedClient {
	return &CachedClient{
		client: client,
		cache:  make(map[string]searchGameCacheEntry),
		ttl:    ttl,
		stop:   make(chan struct{}),
	}
}

func (c *CachedClient) Search(ctx context.Context, name string) ([]domain.BggGameSearch, error) {
	formattedName := formatName(name)

	entry, exists := c.get(formattedName)
	if exists {
		return entry.games, nil
	}

	res, err := c.client.Search(ctx, name)
	if err != nil {
		return nil, fmt.Errorf("failed to search for %s: %w", formattedName, err)
	}

	c.set(formattedName, searchGameCacheEntry{
		games:     res,
		expiresAt: time.Now().Add(c.ttl),
	})

	return res, nil
}

func (c *CachedClient) Thing(ctx context.Context, bggIDs []int64) ([]domain.BggGameThing, error) {
	return c.client.Thing(ctx, bggIDs)
}

func (c *CachedClient) Start(ctx context.Context) {
	go c.cleanupLoop(ctx)
}

func (c *CachedClient) Close() {
	c.stopOnce.Do(func() {
		close(c.stop)
	})
}

func formatName(name string) string {
	name = strings.ToLower(name)
	name = strings.Trim(name, " ")

	return name
}

func (c *CachedClient) get(name string) (searchGameCacheEntry, bool) {
	c.mtx.RLock()
	defer c.mtx.RUnlock()

	entry, ok := c.cache[name]

	if time.Now().After(entry.expiresAt) {
		return searchGameCacheEntry{}, false
	}

	return entry, ok
}

func (c *CachedClient) set(name string, entry searchGameCacheEntry) {
	c.mtx.Lock()
	defer c.mtx.Unlock()

	c.cache[name] = entry
}

func (c *CachedClient) cleanupLoop(ctx context.Context) {
	ticker := time.NewTicker(time.Second * 30)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			c.deleteExpiredCache()
		case <-ctx.Done():
			return
		case <-c.stop:
			return
		}
	}
}

func (c *CachedClient) deleteExpiredCache() {
	c.mtx.Lock()
	defer c.mtx.Unlock()

	now := time.Now()

	for key, entry := range c.cache {
		if now.After(entry.expiresAt) {
			delete(c.cache, key)
		}
	}
}
