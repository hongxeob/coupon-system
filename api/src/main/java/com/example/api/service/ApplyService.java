package com.example.api.service;

import org.springframework.stereotype.Service;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.respoistory.AppliedUserRepository;
import com.example.api.respoistory.CouponCountRepository;
import com.example.api.respoistory.CouponRepository;

@Service
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;
	private final AppliedUserRepository appliedUserRepository;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
		this.couponCreateProducer = couponCreateProducer;
		this.appliedUserRepository = appliedUserRepository;
	}

	public void apply(Long userId) {
		//1인 1쿠폰을 위해서 Redis의 Set 자료구조를 사용.
		Long apply = appliedUserRepository.add(userId);

		//이미 발급한 유저라면 1이 아니라 0이 나올것
		if (apply != 1) {
			return;
		}

		//정상적인 쿠폰 발급 요청이 오면 Redis를 이용해 쿠폰 값을 하나 증가 시킨다.
		Long count = couponCountRepository.increment();

		if (count > 100) {
			return;
		}
		//프로듀서 작업
		couponCreateProducer.create(userId);
	}
}
