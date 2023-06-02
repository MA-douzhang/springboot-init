package com.madou.springbootinit.manager;


import com.madou.springbootinit.common.ErrorCode;
import com.madou.springbootinit.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//todo redis配置
//@Service
public class RedisLimiterManager {

    @Resource
    RedissonClient redissonClient;

    /**
     * 限流操作
     *
     * @param key
     */
    public void doRateLimit(String key) {
        RRateLimiter rRateLimiter = redissonClient.getRateLimiter(key);
        //限流器
        rRateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        //每次消耗1个令牌
        boolean canOp = rRateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
        }

    }

}
