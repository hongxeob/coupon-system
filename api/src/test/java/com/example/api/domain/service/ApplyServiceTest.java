package com.example.api.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.api.domain.respoistory.CouponRepository;

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

	@Test
	void 여려명_응모_레이스_컨디션_발생() throws Exception {

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

		//then
		long count = couponRepository.count();
		assertThat(count).isEqualTo(100);

	}
}
