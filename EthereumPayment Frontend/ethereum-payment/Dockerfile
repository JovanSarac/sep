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
COPY --from=build /app/dist/ethereum-payment/browser /usr/share/nginx/html

# Copy SSL certificate and key
COPY ethpayment-fe.crt /etc/ssl/certs/ethpayment-fe.crt
COPY ethpayment-fe.key /etc/ssl/private/ethpayment-fe.key

# Copy custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Expose HTTPS port
EXPOSE 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
