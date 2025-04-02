FROM node:18-alpine AS build

WORKDIR /app

rm -rf package-lock.json node_modules
RUN npm install
CMD ["ng", "version"]
COPY . .
RUN npm run build --prod

# Use Nginx for serving the Angular app
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

CMD ["ng", "serve"]
