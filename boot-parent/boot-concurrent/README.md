# 高并发分布式Boot

提供高并发分布式相关功能

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

## 任务执行器

`ExecutorService`同时执行多个任务`Executor`，支持依赖执行、失败回滚。
