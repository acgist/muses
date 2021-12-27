# 高并发分布式

## 分布式锁

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

## 分布式定时任务

```
@Scheduled(cron = "*/5 * * * * ?")
@DistributedScheduled(name = "lockName", ttl = 10)
public void scheduled() {
	...
}
```