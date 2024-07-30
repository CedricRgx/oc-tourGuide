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
     *
     * @param gpsUtil Instance of GpsUtil to have location data
     */
    public GpsUtilService(GpsUtil gpsUtil){
        this.gpsUtil = gpsUtil;
    }

    public static ExecutorService executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * (1 + 10000/3), Integer.MAX_VALUE, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

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
