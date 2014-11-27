package de.rwth.dbis.layers.lapps;

import java.math.BigInteger;
import java.util.Random;

import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;

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
    return (int) (Math.random() * (max - min - 1));
  }

  public static String generateRandomString() {
    final Random random = new Random();
    return new BigInteger(80, random).toString();
  }

  public static AppEntity createDummyAppWithInstance(AppPlatformEntity onPlatform) {
    AppInstanceEntity instance = new AppInstanceEntity(onPlatform, "dummy app instance");
    AppEntity app = new AppEntity("dummy app");
    app.addInstance(instance);
    return app;
  }
}
