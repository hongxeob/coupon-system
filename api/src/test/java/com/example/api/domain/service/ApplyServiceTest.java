package com.example.api.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.api.respoistory.CouponRepository;
import com.example.api.service.ApplyService;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Test
	void 한번만_응모() throws Exception {

		//given
		applyService.apply(1L);

		//when
		long count = couponRepository.count();

		//then
		assertThat(count).isEqualTo(1);
	}

	/**
	 * Redis는 싱글 스레드 기반 이므로 각 스레드는 항상 최신화된 정보를 가지고 시작할 수 있다.
	 * 하지만 많은 양이 한번에 들어오면 타임아웃 상황이 발생할 수 있다.z
	 */
	@Test
	void 여려명_응모() throws Exception {

		//given
		int threadCount = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		//when
		for (int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(userId);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Thread.sleep(10000);

		long count = couponRepository.count();

		//then
		assertThat(count).isEqualTo(100);
	}

	@Test
	void 일인_일쿠폰() throws Exception {

		//given
		int threadCount = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		//when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					applyService.apply(1L);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Thread.sleep(10000);

		long count = couponRepository.count();

		//then
		assertThat(count).isEqualTo(1);
	}
}
