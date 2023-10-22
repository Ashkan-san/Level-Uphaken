FROM node:alpine
RUN apk add --no-cache libc6-compat
USER node
ENV NODE_ENV development
ENV NEXT_TELEMETRY_DISABLED 1
COPY .docker/development-entrypoint.sh /
VOLUME /app
WORKDIR /app
EXPOSE 3000
CMD ["/development-entrypoint.sh"]