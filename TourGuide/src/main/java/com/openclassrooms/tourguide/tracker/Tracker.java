package com.openclassrooms.tourguide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.user.User;

/**
 * Tracker class for tracking user locations at regular intervals
 */
public class Tracker extends Thread {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

	/**
	 * Constructs a Tracker with the given TourGuideService
	 *
	 * @param tourGuideService the TourGuideService to use for tracking user locations
	 */
	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;

		executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	/**
	 * Runs the tracking process, periodically updating the location of all users
	 */
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			// Check if the current thread is interrupted or the stop flag is set
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			// Retrieve the list of all users
			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			// Start the stopwatch to measure the time taken for tracking
			stopWatch.start();
			// Track the location for each user
			users.forEach(u -> tourGuideService.trackUserLocation(u));
			// Stop the stopwatch
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			// Reset the stopwatch for the next iteration
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				// Sleep for the specified polling interval
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}

	}
}
