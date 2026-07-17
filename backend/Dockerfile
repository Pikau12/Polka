# Build
FROM golang:1.26.5-alpine AS build
WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o /server .

# Test
FROM build AS test
RUN go test -v ./...

# Run
FROM alpine:latest
WORKDIR /app

COPY --from=build /server /app/server

EXPOSE 8080
ENTRYPOINT ["/app/server"]
