package com.example.consumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedEventRepository;

@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;
	private final FailedEventRepository failedEventRepository;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
		this.couponRepository = couponRepository;
		this.failedEventRepository = failedEventRepository;
	}

	@KafkaListener(topics = "coupon_create", groupId = "group_1")
	public void listener(Long userId) {
		try {
			couponRepository.save(new Coupon(userId));
		} catch (Exception e) {
			log.error("쿠폰 생성 중 오류 발생 userId => ", userId);
			failedEventRepository.save(new FailedEvent(userId));
		}
	}
}
