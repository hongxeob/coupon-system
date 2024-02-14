package com.example.api.service;

import org.springframework.stereotype.Service;

import com.example.api.domain.Coupon;
import com.example.api.respoistory.CouponCountRepository;
import com.example.api.respoistory.CouponRepository;

@Service
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
	}

	public void apply(Long userId) {
		//쿠폰 발급 요청이 오면 Redis를 이용해 쿠폰 값을 하나 증가 시킨다.
		Long count = couponCountRepository.increment();

		if (count > 100) {
			return;
		}

		couponRepository.save(new Coupon(userId));
	}
}
