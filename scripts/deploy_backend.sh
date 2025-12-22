#!/bin/bash

set -e

echo "📂 Loading environment variables..."

if [ -f .env ]; then
    while IFS='=' read -r key value || [ -n "$key" ]; do
        [[ $key =~ ^#.*$ || -z $key ]] && continue
        value=$(echo "$value" | tr -d '\r' | sed -e 's/^"//' -e 's/"$//')
        export "$key=$value"
    done < .env
else
    echo "❌ Error: .env file not found."
    exit 1
fi

echo "🔍 Verification: Connecting to ${SERVER_USER}@${SERVER_IP}..."

if [ -z "$SERVER_USER" ] || [ -z "$SERVER_IP" ]; then
    echo "❌ Error: SERVER_USER or SERVER_IP is empty. Check your .env file format."
    exit 1
fi

echo "🚀 Starting Deployment..."

echo "🐳 Building Docker Image (linux/amd64)..."
docker buildx build --platform linux/amd64 -t mysterybox-backend --load .

echo "📤 Uploading Image to Server..."
docker save mysterybox-backend | gzip | ssh "${SERVER_USER}@${SERVER_IP}" "gunzip | docker load"

echo "🔄 Restarting Container..."
ssh "${SERVER_USER}@${SERVER_IP}" << EOF
    set -e
    docker stop mysterybox-api || true
    docker rm mysterybox-api || true

    docker run -d \
      --name mysterybox-api \
      --restart always \
      -p 8080:9090 \
      -e DATABASE_URL="$DB_URL" \
      -e DATABASE_USER="$DB_USER" \
      -e DATABASE_PASSWORD="$DB_PASSWORD" \
      -e JWT_SECRET="$JWT_SECRET" \
      -e JWT_ISSUER="$JWT_ISSUER" \
      -e JWT_AUDIENCE="$JWT_AUDIENCE" \
      mysterybox-backend

    docker image prune -f
EOF

echo "✅ Deployment Complete!"
