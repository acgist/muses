# 消息队列Boot

提供消息队列功能

## Kafka

默认使用`Kafka`

#### 配置

```
# 配置参考：https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/current/reference/html/spring-cloud-stream-binder-kafka.html
# 注意：如果消费者数量大于分区数量部分消费者将不会收到消息
# ./bin/windows/kafka-topics.bat --zookeeper localhost:2181 --list
# ./bin/windows/kafka-topics.bat --zookeeper localhost:2181 --topic topic-gateway --describe
# ./bin/windows/kafka-topics.bat --zookeeper localhost:2181 --topic topic-gateway -alter --partitions 3
# ./bin/windows/kafka-topics.bat --zookeeper localhost:2181 --topic topic-gateway --create --partitions 3 --replication-factor 3
# 以下配置效果一致：多个逗号分隔
#spring.kafka.bootstrapServers=localhost:9092
#spring.cloud.stream.kafka.binder.brokers=localhost:9092
#spring.cloud.stream.kafka.binder.zk-nodes=localhost:2181
#spring.cloud.stream.kafka.streams.binder.brokers=localhost:9092
#spring.cloud.stream.kafka.streams.binder.zk-nodes=localhost:2181
# 生产者
#spring.cloud.stream.kafka.default.producer.sync=false
#spring.cloud.stream.kafka.default.producer.bufferSize=1024
# 消费者
#spring.cloud.stream.kafka.default.consumer.ack-mode=RECORD
#spring.cloud.stream.kafka.default.consumer.dlq-name=dlqName
#spring.cloud.stream.kafka.default.consumer.enable-dlq=false
#spring.cloud.stream.kafka.default.consumer.poll-timeout=5
#spring.cloud.stream.kafka.default.consumer.start-offset=earliest
#spring.cloud.stream.kafka.default.consumer.auto-commit-offset=true
# binder
#spring.cloud.stream.kafka.binder.required-acks=1
#spring.cloud.stream.kafka.binder.auto-create-topics=false
#spring.cloud.stream.kafka.binder.replication-factor=3
#spring.cloud.stream.kafka.binder.min-partition-count=3
# binders
#spring.cloud.stream.binders.gateway.type: kafka
#spring.cloud.stream.binders.gateway.environment.spring.cloud.stream.kafka.streams.binder.brokers=localhost:9092
# 默认
#spring.cloud.stream.default-binder=gateway
# function
#spring.cloud.stream.function.definition=gatewayRecord;gatewayRecordCopy
# 批量
#spring.cloud.stream.binding.gateway.consumer.batch-mode=true
# bindings
#spring.cloud.stream.bindings.gatewayRecord-in-0.group=spring
#spring.cloud.stream.bindings.gatewayRecord-in-0.binder=gateway
#spring.cloud.stream.bindings.gatewayRecord-in-0.destination=topic-gateway
#spring.cloud.stream.bindings.gatewayRecordCopy-in-0.group=spring
#spring.cloud.stream.bindings.gatewayRecordCopy-in-0.binder=gateway
#spring.cloud.stream.bindings.gatewayRecordCopy-in-0.destination=topic-gateway
```

## RocketMQ

```
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-starter-stream-rocketmq</artifactId>
</dependency>
```

## RabbitMQ

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```
