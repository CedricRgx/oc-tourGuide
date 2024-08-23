package com.openclassrooms.tourguide;

import com.openclassrooms.tourguide.service.GpsUtilService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
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
	public GpsUtilService getGpsUtil() {
		return new GpsUtilService();
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
