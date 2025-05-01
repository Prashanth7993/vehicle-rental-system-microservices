FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install && npm install -g @angular/cli
COPY . .
RUN npm run build 

FROM nginx:alpine
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/onthego/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
