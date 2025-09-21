# Build stage
FROM golang:1.24-alpine AS build
WORKDIR /app

# Install dependencies
COPY go.mod go.sum ./
RUN go mod download

# Copy source code
COPY . .

# Build binary
RUN go build -o paypal-service main.go

# Runtime stage
FROM alpine:3.18
WORKDIR /app

# Copy the built binary
COPY --from=build /app/paypal-service .

# Expose port
EXPOSE 8086

# Run the service
ENTRYPOINT ["./paypal-service"]
