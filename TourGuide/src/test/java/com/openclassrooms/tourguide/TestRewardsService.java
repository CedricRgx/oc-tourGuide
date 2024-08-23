package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.openclassrooms.tourguide.service.GpsUtilService;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

public class TestRewardsService {
	private static final Logger logger = LoggerFactory.getLogger(TestRewardsService.class);

	@Test
	public void userGetRewards() {
		GpsUtilService gpsUtilService = new GpsUtilService();
		GpsUtilService getUserLocationService = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		GpsUtilService gpsUtilService = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	//@Disabled // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		GpsUtilService gpsUtilService = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = tourGuideService.getAllUsers().get(0);

		rewardsService.calculateRewards(user);
		tourGuideService.tracker.stopTracking();

		// List of attractions of the user last position in a range of 10 miles
		List<Attraction> nearAttractionsList = gpsUtil.getAttractions().stream().filter(a -> rewardsService.isNearAttraction(user.getLastVisitedLocation(), a)).toList();

		assertEquals(gpsUtil.getAttractions().size(), nearAttractionsList.size());
	}

}