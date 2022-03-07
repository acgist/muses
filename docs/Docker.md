# Docker

## wait-for

[https://github.com/mrako/wait-for](https://github.com/mrako/wait-for)

## docker-compose

```
version: "3.1"

services:

  service-user-impl:
    container_name: service-user-impl
    image: openjdk:17.0.2
    restart: always
    privileged: true
#   解决注册内网IP问题
#   network_mode: host
    command: sh /data/app/bin/startup.sh;/wait-for service-user-impl:9999 -- echo 'success'
    expose:
      - 9999
    volumes:
      - /etc/localtime:/etc/localtime:ro
      - $PWD/service-user-impl:/data/app
      - $PWD/wait-for:/wait-for
    environment:
      - TZ=Asia/Shanghai
    external_links:
      - nacos
      - mysql

  rest-oauth2:
    container_name: rest-oauth2
    image: openjdk:17.0.2
    restart: always
    privileged: true
    command: sh /data/app/bin/startup.sh
    links:
      - service-user-impl
    expose:
      - 8080
    volumes:
      - /etc/localtime:/etc/localtime:ro
      - $PWD/rest-oauth2:/data/app
      - $PWD/wait-for:/wait-for
    depends_on:
      - service-user-impl
    environment:
      - TZ=Asia/Shanghai
    external_links:
      - nacos

networks:
  app_net:
    external: true
  default:
    name: app_net
```