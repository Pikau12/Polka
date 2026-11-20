package storage

import (
	"context"
	"fmt"
	"io"

	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/credentials"
	"github.com/aws/aws-sdk-go-v2/service/s3"
	"github.com/polka/backend/internal/config"
	"github.com/polka/backend/internal/domain"

	awsconfig "github.com/aws/aws-sdk-go-v2/config"
)

type Garage struct {
	client *s3.Client
	bucket string
}

type Object struct {
	Body          io.ReadCloser
	ContentType   string
	ContentLength int64
}

func NewGarage(ctx context.Context, cfg config.GarageConfig) (*Garage, error) {
	awsCfg, err := awsconfig.LoadDefaultConfig(
		ctx,
		awsconfig.WithRegion(cfg.Region),
		awsconfig.WithCredentialsProvider(credentials.NewStaticCredentialsProvider(
			cfg.AccessKey,
			cfg.SecretKey,
			""),
		),
	)

	if err != nil {
		return nil, fmt.Errorf("failed to load aws config: %w", err)
	}

	client := s3.NewFromConfig(awsCfg, func(o *s3.Options) {
		o.BaseEndpoint = aws.String(cfg.Endpoint)
		o.UsePathStyle = true
	})

	_, err = client.HeadBucket(ctx, &s3.HeadBucketInput{
		Bucket: aws.String(cfg.Bucket),
	})

	if err != nil {
		return nil, fmt.Errorf("failed to check if bucket exists: %w", err)
	}

	return &Garage{client: client, bucket: cfg.Bucket}, nil
}

func (g *Garage) Put(ctx context.Context, key string, body io.Reader, contentType string, size int64) (*domain.StoredObject, error) {
	_, err := g.client.PutObject(ctx, &s3.PutObjectInput{
		Bucket:        aws.String(g.bucket),
		Key:           aws.String(key),
		Body:          body,
		ContentLength: aws.Int64(size),
		ContentType:   aws.String(contentType),
	})

	if err != nil {
		return nil, fmt.Errorf("failed to put object: %q %w", key, err)
	}

	return &domain.StoredObject{Bucket: g.bucket, ObjectKey: key}, nil
}

func (g *Garage) Get(ctx context.Context, key string) (*Object, error) {
	data, err := g.client.GetObject(ctx, &s3.GetObjectInput{
		Bucket: aws.String(g.bucket),
		Key:    aws.String(key),
	})

	if err != nil {
		return nil, fmt.Errorf("failed to get object: %q %w", key, err)
	}

	return &Object{
		Body:          data.Body,
		ContentType:   aws.ToString(data.ContentType),
		ContentLength: aws.ToInt64(data.ContentLength),
	}, nil
}

func (g *Garage) Delete(ctx context.Context, key string) error {
	_, err := g.client.DeleteObject(ctx, &s3.DeleteObjectInput{
		Bucket: aws.String(g.bucket),
		Key:    aws.String(key),
	})

	if err != nil {
		return fmt.Errorf("failed to delete object: %q %w", key, err)
	}

	return nil
}
