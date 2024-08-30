package com.openclassrooms.tourguide;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.service.RewardsService;

/**
 * Configuration class for TourGuide application beans
 */
@Configuration
public class TourGuideModule {

	/**
	 * Bean for GpsUtil.
	 *
	 * @return a new instance of GpsUtil
	 */
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	/**
	 * Bean for RewardsService
	 *
	 * @return a new instance of RewardsService
	 */
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}

	/**
	 * Bean for RewardCentral
	 *
	 * @return a new instance of RewardCentral
	 */
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
