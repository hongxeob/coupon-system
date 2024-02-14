package com.example.api.domain.respoistory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponCountRepository {
	private final RedisTemplate<String, String> redisTemplate;

	public CouponCountRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Long increment() {
		return redisTemplate
			.opsForValue()
			.increment("coupon.count");
	}
}
