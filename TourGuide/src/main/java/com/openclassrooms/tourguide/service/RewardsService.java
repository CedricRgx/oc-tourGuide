package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import static com.openclassrooms.tourguide.service.GpsUtilService.executor;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Service for managing rewards in the TourGuide application
 */
@Service
public class RewardsService {
	private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtilService gpsUtilService;
	private final RewardCentral rewardsCentral;

	/**
	 * Constructs a RewardsService with the given dependencies
	 *
	 * @param gpsUtilService the GPS utility service
	 * @param rewardCentral the rewards central service
	 */
	public RewardsService(GpsUtilService gpsUtilService, RewardCentral rewardCentral) {
		this.gpsUtilService = gpsUtilService;
		this.rewardsCentral = rewardCentral;
	}

	/**
	 * Sets the proximity buffer for calculating rewards
	 *
	 * @param proximityBuffer the proximity buffer in miles
	 */
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	/**
	 * Sets the proximity buffer to its default value
	 */
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * Calculates rewards for a given user based on their visited locations and nearby attractions
	 *
	 * @param user the user for whom rewards are calculated
	 */
	public void calculateRewards(User user) {
		CopyOnWriteArrayList<VisitedLocation> userLocationsCopy = new CopyOnWriteArrayList<>(user.getVisitedLocations()); // create a copy of list of locations (getVisitedLocations)
		List<Attraction> attractions = gpsUtilService.getAttractions();
		for(VisitedLocation visitedLocation : userLocationsCopy) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
						/*getRewardPointsAsync(attraction, user).thenAccept(rewardPoints -> {
							user.addUserReward(new UserReward(visitedLocation, attraction, rewardPoints));
						});*/
						//calculateDistanceReward(user, visitedLocation, attraction);
					}
				}
			}
		}
	}

/*	public void calculateDistanceReward(User user, VisitedLocation visitedLocation, Attraction attraction) {
		Double distance = getDistance(attraction, visitedLocation.location);
		UserReward userReward = new UserReward(visitedLocation, attraction, distance.intValue());
		executor.submit(() -> {
			getRewardPointsAsync(userReward, attraction, user);
		});
	}*/

	public int getRewardPoints(Attraction attraction, User user) {
		CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
					return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
				}, executor);
        try {
            return completableFuture.get();
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        } catch(ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Calculates reward points for a user visiting an attraction blocking until completion
	 *
	 * @param attraction the Attraction being visited
	 * @param user the User who is visiting the attraction
	 *
	 * @return the reward points earned, or 0 in case of error
	 */
/*	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}*/

	/**
	 * Asynchronously calculates reward points for a user at an attraction
	 *
	 * @param attraction The Attraction of interest
	 *
	 * @param user The User to calculate reward points for
	 * @return Returns a CompletableFuture of reward points
	 */
	public CompletableFuture<Integer> getRewardPointsAsync(Attraction attraction, User user) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, executor);
    }

	/**
	 * Checks if a location is within proximity of an attraction
	 *
	 * @param attraction the attraction to check
	 * @param location the location to check
	 * @return true if the location is within proximity of the attraction, false otherwise
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	/**
	 * Checks if a visited location is near an attraction
	 *
	 * @param visitedLocation the visited location to check
	 * @param attraction the attraction to check
	 * @return true if the visited location is near the attraction, false otherwise
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	/**
	 * Calculates the distance between two locations in statute miles
	 *
	 * @param loc1 the first location
	 * @param loc2 the second location
	 * @return the distance between the two locations in statute miles
	 */
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

	/**
	 * Gets the reward points for a user at a given attraction
	 *
	 * @param attraction the attraction to check
	 * @param user the user to check
	 * @return the reward points for the user at the attraction
	 */
	public int getPoints(Attraction attraction, User user) {
		return getRewardPoints(attraction, user);
	}

	/**
	 * Checks if a visited location is near an attraction
	 *
	 * @param visitedLocation the visited location to check
	 * @param attraction the attraction to check
	 * @return true if the visited location is near the attraction, false otherwise
	 */
	public boolean isNearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

}
