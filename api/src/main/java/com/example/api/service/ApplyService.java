package com.example.api.service;

import org.springframework.stereotype.Service;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.respoistory.CouponCountRepository;
import com.example.api.respoistory.CouponRepository;

@Service
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
		this.couponCreateProducer = couponCreateProducer;
	}

	public void apply(Long userId) {
		//쿠폰 발급 요청이 오면 Redis를 이용해 쿠폰 값을 하나 증가 시킨다.
		Long count = couponCountRepository.increment();

		if (count > 100) {
			return;
		}
		//프로듀서 작업
		couponCreateProducer.create(userId);
	}
}
