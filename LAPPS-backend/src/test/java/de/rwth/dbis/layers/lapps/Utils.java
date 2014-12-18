package de.rwth.dbis.layers.lapps;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class Utils {
  public static final String DELETE_USERS_QUERY = "delete UserEntity";
  public static final String DELETE_APPS_QUERY = "delete AppEntity";

  /**
   * Generates a random integer in a range (min-max)
   * 
   * @param min Minimal range value
   * @param max Maximal range value
   * @return an int in the range (min-max)
   */
  public static int generateRandomInt(int min, int max) {
    return (int) (min + Math.random() * (max - min - 1));
  }

  public static String generateRandomString() {
    final Random random = new Random();
    return new BigInteger(80, random).toString();
  }

  public static <E> E getRandomEntity(List<E> list) {
    return list.get(generateRandomInt(0, list.size()));
  }

  public static <E> E removeRandomEntity(List<E> list) {
    return list.remove(generateRandomInt(0, list.size()));
  }

  // run mvn test -Dtest=Utils to generate mockup data in DB
  @Test
  public void generate() {
    DataGenerator.main(new String[] {"100"});
  }
  /*
   * @Test public void mockupAppGenerator() { for (int i = 0; i < 2; i++) { //
   * System.out.println(DataGeneratorUtils.getRandomName());
   * System.out.println(DataGeneratorUtils.getRandomPlatform());
   * System.out.println(DataGeneratorUtils.getRandomUrl());
   * System.out.println(DataGeneratorUtils.getRandomLicense());
   * 
   * 
   * System.out.println(DataGeneratorUtils.getRandomShortDescription());
   * System.out.println(DataGeneratorUtils.getRandomLongDescription());
   * System.out.println(DataGeneratorUtils.getRandomVersion());
   * System.out.println(DataGeneratorUtils.getRandomMinPlatform());
   * System.out.println(DataGeneratorUtils.getRandomRating());
   * System.out.println(DataGeneratorUtils.getRandomSize());
   * System.out.println(DataGeneratorUtils.combine(DataGeneratorUtils.getRandomTags(2, 8), " , "));
   * 
   * System.out.println(DataGeneratorUtils.getRandomThumbnailUrl());
   * System.out.println(DataGeneratorUtils.getRandomImageDescription()); System.out
   * .println(DataGeneratorUtils.combine(DataGeneratorUtils.getRandomImageUrls(5), " , "));
   * 
   * System.out.println(" "); System.out.println(" "); User usr =
   * DataGeneratorUtils.getRandomUser(); System.out.println(usr.getOidcId());
   * System.out.println(usr.getUsername()); System.out.println(usr.getEmail());
   * System.out.println(usr.getRole()); System.out.println(" "); System.out.println(" "); } }
   */
}
