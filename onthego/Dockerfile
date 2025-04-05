# Step 1: Build the Angular app
FROM node:18-alpine AS build

WORKDIR /app

# Copy package.json and package-lock.json first (for caching)
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install
RUN npm install -g @angular/cli

# startp]ject
COPY . .

# Build the Angular app for production
RUN npm run build --configuration=production --output-path=dist

# Step 2: Use Nginx to serve the built Angular app
FROM nginx:alpine

# Copy custom Nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built Angular app to Nginx's default static directory
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 4200

# Start Nginx
CMD ["ng", "serve"]

