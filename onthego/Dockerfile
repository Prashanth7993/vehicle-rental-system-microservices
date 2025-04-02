# Step 1: Build the Angular app
FROM node:18-alpine AS build

WORKDIR /app

# Copy package.json and package-lock.json first for better caching
COPY package.json  ./

RUN npm install --package-lock-only
# Install dependencies
RUN npm install

# Copy the entire project
COPY . .

# Build the Angular app
RUN npm run build --configuration=production

# Step 2: Use Nginx to serve the built Angular app
FROM nginx:alpine

# Copy built Angular app to Nginx's default static directory
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]

