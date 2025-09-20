# 1. Build stage
FROM golang:1.23-alpine AS build
WORKDIR /app

# Kopiraj mod fajlove i povuci dependencije
COPY go.mod go.sum ./
RUN go mod download

# Kopiraj kod
COPY . .

# Build
RUN go build -o apigateway main.go

# 2. Runtime stage
FROM alpine:latest
WORKDIR /app

# Kopiraj binarni fajl i sertifikate
COPY --from=build /app/apigateway .
COPY apigateway.crt .
COPY apigateway.key .

# Expose port
EXPOSE 9001

# Run
CMD ["./apigateway"]