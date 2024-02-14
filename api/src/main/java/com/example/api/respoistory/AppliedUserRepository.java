package com.example.api.respoistory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppliedUserRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public AppliedUserRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	//Redis의 Set자료구조 사용
	public Long add(Long userId) {
		return redisTemplate
			.opsForSet()
			.add("applied_user", userId.toString());
	}
}
