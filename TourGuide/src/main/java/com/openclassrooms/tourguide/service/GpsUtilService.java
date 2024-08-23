package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.user.User;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * Service for fetching user location and nearby attractions using GpsUtil
 */
@Service
public class GpsUtilService {

    private Logger logger = LoggerFactory.getLogger(GpsUtilService.class);

    private final GpsUtil gpsUtil;

    /**
     * Constructor for GetUserLocationService
     */
    public GpsUtilService(){
        this.gpsUtil = new GpsUtil();
    }

    //public static ExecutorService executor = Executors.newFixedThreadPool(1000);

    // Creating an Executor Service named 'executor'
    public static ExecutorService executor =
        // ThreadPoolExecutor is being used for the executor
        new ThreadPoolExecutor(
            // Initial pool size - the number of threads to keep in the pool, even if they are inactive,
            // Number of available processors (cores) multiplied by waitTime/serviceTime (i.e. the ratio
            // of the time a task spends waiting in the executor queue (waitTime) to the time it takes
            // for a thread to complete the task (serviceTime))
            Runtime.getRuntime().availableProcessors() * (1 + 10000/3),
            // Maximum allowed pool size
            Integer.MAX_VALUE,
            // Keep alive time for inactive threads when number of threads is more than the core pool size
            30,
            // Unit for keep alive time
            TimeUnit.SECONDS,
            // Task queue buffer to hold tasks before they are executed
            new LinkedBlockingQueue<>());

    /**
     * Retrieves the current location of a user asynchronously and
     * submits this location using the tourGuideService
     *
     * @param user The user for whom the visited location is being fetched and logged
     * @return tourGuideService The service that contains the logic for logging the visited location
     */
    public void getFinalizeLocation(User user, TourGuideService tourGuideService) {
        CompletableFuture.supplyAsync(() -> {
            VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
            return visitedLocation;
        }, executor).thenAccept(visitedLocation -> {
            tourGuideService.submitLocation(user, visitedLocation);
        });
    }

    /**
     * Retrieves a list of nearby attractions asynchronously
     *
     * @return A CompletableFuture containing a list of attractions
     */
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

}
