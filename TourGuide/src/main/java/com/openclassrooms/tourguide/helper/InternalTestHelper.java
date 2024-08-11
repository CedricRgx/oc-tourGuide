package com.openclassrooms.tourguide.helper;

/**
 * Helper class for managing internal test configurations
 */
public class InternalTestHelper {

	// Set this default up to 100,000 for testing
	private static int internalUserNumber = 10000;//100;

	/**
	 * Sets the number of internal users for testing
	 *
	 * @param internalUserNumber the number of internal users
	 */
	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}

	/**
	 * Gets the number of internal users for testing
	 *
	 * @return the number of internal users
	 */
	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}
