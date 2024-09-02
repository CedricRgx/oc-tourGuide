package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.tracker.Tracker;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

import tripPricer.Provider;
import tripPricer.TripPricer;


/**
 * Service for managing users and their interactions with attractions, rewards, and trip deals in the TourGuide application
 */
@Service
public class TourGuideService {
	public static ExecutorService executor = Executors.newFixedThreadPool(10000);

/*	public static ExecutorService executor =
			// ThreadPoolExecutor is being used for the executor
			new ThreadPoolExecutor(
					// Initial pool size - the number of threads to keep in the pool, even if they are inactive,
					// Number of available processors (cores) multiplied by waitTime/serviceTime (i.e. the ratio
					// of the time a task spends waiting in the executor queue (waitTime) to the time it takes
					// for a thread to complete the task (serviceTime))
					Runtime.getRuntime().availableProcessors() * (1 + 10000 / 3),
					// Maximum allowed pool size
					Integer.MAX_VALUE,
					// Keep alive time for inactive threads when number of threads is more than the core pool size
					30,
					// Unit for keep alive time
					TimeUnit.SECONDS,
					// Task queue buffer to hold tasks before they are executed
					new LinkedBlockingQueue<>());*/

	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	boolean testMode = true;


	/**
	 * Constructs a TourGuideService with the given dependencies
	 *
	 * @param gpsUtil        the GPS utility service
	 * @param rewardsService the rewards service
	 */
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.rewardsService = rewardsService;
		this.gpsUtil = gpsUtil;

		Locale.setDefault(Locale.US);

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	/**
	 * Retrieves the list of rewards for a given user
	 *
	 * @param user the user to retrieve rewards for
	 * @return the list of user rewards
	 */
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	/**
	 * Retrieves the current location of a user
	 *
	 * @param user the user to retrieve the location for
	 * @return the visited location of the user
	 */
	public VisitedLocation getUserLocation(User user) {
		Object visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return (VisitedLocation) visitedLocation;
	}

	/**
	 * Retrieves a user by their username
	 *
	 * @param userName the username of the user
	 * @return the user with the given username
	 */
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	/**
	 * Retrieves a list of all users
	 *
	 * @return the list of all users
	 */
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Adds a user to the internal user map if they are not already present
	 *
	 * @param user the user to add
	 */
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * Retrieves trip deals for a user based on their rewards points
	 *
	 * @param user the user to retrieve trip deals for
	 * @return the list of trip providers
	 */
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Tracks the user's current location asynchronously and calculates rewards based on the location
	 *
	 * The user's location is retrieved and added to their visited locations
	 * Rewards are then calculated for the user based on the new location
	 *
	 * @param user the user whose location is being tracked
	 * @return a `CompletableFuture<VisitedLocation>` that completes with the user's visited location once it is tracked and rewards are calculated
	 */
	public CompletableFuture<VisitedLocation> trackUserLocation(User user) {
		CompletableFuture<VisitedLocation> completableFutureVisitedLocation = CompletableFuture.supplyAsync(() -> {
			VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
			return visitedLocation;
		}, executor).thenApplyAsync((visitedLocation) -> {
			user.addToVisitedLocations(visitedLocation);
			rewardsService.calculateRewards(user).join();
			return visitedLocation;
		}, rewardsService.getExecutorRewardsService());
		return completableFutureVisitedLocation;
	}

	/**
	 * Retrieves a list of nearby attractions for a given visited location.
	 *
	 * @param visitedLocation the visited location to find nearby attractions for
	 * @return the list of nearby attractions
	 */
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		List<Attraction> listAttractions = gpsUtil.getAttractions();
		for (Attraction attraction : listAttractions) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}
		return nearbyAttractions;
	}

	/**
	 * Adds a shutdown hook to stop the tracker when the application is stopped
	 */
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}

	/**********************************************************************************
	 *
	 * Methods Below: For Internal Testing
	 *
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	/**
	 * Initializes internal users for testing purposes
	 */
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	/**
	 * Generates a random location history for a user
	 *
	 * @param user the user to generate location history for
	 */
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 * Generates a random longitude value
	 *
	 * @return a random longitude
	 */
	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Generates a random latitude value
	 *
	 * @return a random latitude
	 */
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Generates a random date within the past 30 days
	 *
	 * @return a random date
	 */
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
