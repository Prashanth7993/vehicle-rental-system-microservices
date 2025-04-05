# Step 1: Build the Angular app
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install && npm install -g @angular/cli
COPY . .
RUN npm run build 


FROM nginx:alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build /app/dist/onthego/browser .
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

