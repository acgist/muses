## 高并发分布式

#### 并发任务

```
CompletableFuture.supplyAsync
```

#### 分布式锁

```
@Autowired
private DistributedLock distributedLock;

try {
	if (this.distributedLock.tryLock("acgist", 10)) {
		// 成功
	} else {
		// 失败
	}
} finally {
	this.distributedLock.unlock("acgist");
}
```

#### 分布式的定时任务

```
@Scheduled(cron = "*/5 * * * * ?")
@DistributedScheduled(name = "lockName", ttl = 10)
public void scheduledGroupB() {
	System.out.println("scheduledGroupB");
}
```