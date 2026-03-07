# Build stage
FROM node:20-alpine AS build
WORKDIR /app

# API URL: empty = same origin (nginx proxy)
ARG REACT_APP_API_URL=
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Cognito (baked in at build time)
ARG REACT_APP_USER_POOL_ID=
ARG REACT_APP_USER_POOL_CLIENT_ID=
ARG REACT_APP_AWS_REGION=
ARG REACT_APP_DEV_USER_ID=
ARG REACT_APP_IDENTITY_POOL_ID=

COPY package*.json ./
RUN npm install

COPY . .
# Create .env from build args so Create React App picks them up (compose passes args from host .env)
RUN echo "REACT_APP_API_URL=$REACT_APP_API_URL" > .env && \
    echo "REACT_APP_USER_POOL_ID=$REACT_APP_USER_POOL_ID" >> .env && \
    echo "REACT_APP_USER_POOL_CLIENT_ID=$REACT_APP_USER_POOL_CLIENT_ID" >> .env && \
    echo "REACT_APP_AWS_REGION=$REACT_APP_AWS_REGION" >> .env && \
    echo "REACT_APP_DEV_USER_ID=$REACT_APP_DEV_USER_ID" >> .env && \
    echo "REACT_APP_IDENTITY_POOL_ID=$REACT_APP_IDENTITY_POOL_ID" >> .env
RUN npm run build

# Serve stage
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
RUN echo 'server { \
  listen 80; \
  root /usr/share/nginx/html; \
  index index.html; \
  location / { try_files $uri $uri/ /index.html; } \
  location /api { proxy_pass http://backend:8080; proxy_http_version 1.1; proxy_set_header Host $host; proxy_set_header X-Real-IP $remote_addr; proxy_set_header X-User-Id $http_x_user_id; } \
}' > /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
