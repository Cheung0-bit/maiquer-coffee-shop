# !/bin/sh
docker kill maiqu # 杀死进程
docker rm maiqu # 清除容器
docker run -d -it -v /root/apiclient_key.pem:/root/apiclient_key.pem --name maiqu maiqu/metric-core # 启动容器
docker network connect web_serv maiqu # 连接网络
docker restart caddy # 重启caddy
docker images | grep none | awk '{print $3}' | xargs docker rmi # 清理无用镜像