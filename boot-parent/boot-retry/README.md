# 重试Boot

提供重试功能

## 使用方式

```
@Retryable(value = Exception.class, maxAttempts = 10, backoff = @Backoff(delay = 1000, maxDelay = 5000, multiplier = 2))
public void retry() {
}
```