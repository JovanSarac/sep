# Stage 1: Build Angular app
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# Build production sa base-href
RUN npm run build -- --configuration production --base-href=/

# Stage 2: Serve with Nginx + SSL
FROM nginx:alpine
WORKDIR /etc/nginx

# Copy built Angular app
COPY --from=build /app/dist/bank1 /usr/share/nginx/html

# Copy SSL certificate and key
COPY bank1-fe.crt /etc/ssl/certs/bank1-fe.crt
COPY bank1-fe.key /etc/ssl/private/bank1-fe.key

# Copy custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Expose HTTPS port
EXPOSE 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
