FROM node:18-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build --prod

# Use Nginx for serving the Angular app
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

CMD ["ng", "serve"]
