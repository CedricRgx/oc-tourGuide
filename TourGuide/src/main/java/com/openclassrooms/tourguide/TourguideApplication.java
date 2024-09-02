package com.openclassrooms.tourguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The TourguideApplication is used to launch the TourGuide application
 */
@SpringBootApplication
public class TourguideApplication {

	/**
	 * The entry point of the Tourguide application. This method starts the Spring Boot application
	 *
	 * @param args the command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(TourguideApplication.class, args);
	}

}
