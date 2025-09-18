# Stage 1: Build Angular app
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# Dodaj base-href eksplicitno
RUN npm run build -- --configuration production --base-href=/

# Stage 2: Serve with Nginx + SSL
FROM nginx:alpine
WORKDIR /etc/nginx

# Copy built Angular app
COPY --from=build /app/dist/vivo-net /usr/share/nginx/html

# Copy SSL certificate and key
COPY vivonet-fe.crt /etc/ssl/certs/vivonet-fe.crt
COPY vivonet-fe.key /etc/ssl/private/vivonet-fe.key

# Copy custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Expose HTTPS port
EXPOSE 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
