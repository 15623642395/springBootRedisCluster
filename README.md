演示RedisTemplate集群连接并操作reids
1、开启虚拟机clusterServers
2、启动redis集群
   启动每个redis实例(虚拟机每次重启之后需要重新开启每个redis)
    (1)cd /usr/local/redis-cluster/redis-1/bin/
        --分别为redis-1到redis-6
    (2)./redis-server redis.conf
       --启动redis
    (3)Xshell分别启动六个窗口，分别启动一个reids实例即可
