package de.rwth.dbis.layers.lapps;

import java.math.BigInteger;
import java.util.Random;

public class Utils {
  /**
   * Generates a random integer in a range (min-max)
   * 
   * @param min Minimal range value
   * @param max Maximal range value
   * @return an int in the range (min-max)
   */
  public static int generateRandomInt(int min, int max) {
    return (int) (Math.random() * (max - min - 1));
  }

  public static String generateRandomString() {
    final Random random = new Random();
    return new BigInteger(80, random).toString();
  }
}
