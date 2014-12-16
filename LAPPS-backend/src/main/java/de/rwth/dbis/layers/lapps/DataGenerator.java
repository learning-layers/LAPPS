package de.rwth.dbis.layers.lapps;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.Artifact;
import de.rwth.dbis.layers.lapps.entity.Tag;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Generates some dummy data based on the count of apps requested.
 * 
 * @param 1 - the count of apps requested
 */
public class DataGenerator {
  private static Logger LOGGER = Logger.getLogger(DataGenerator.class.getName());
  private static Facade facade = new Facade();
  private static int DEFAULT_APP_COUNT = 30;
  private static int appCount = DEFAULT_APP_COUNT;

  public static void main(String[] args) {
    if (args[0] != null && args[0] != "") {
      appCount = Integer.parseInt(args[0]);
    }
    dropExistingData();
    List<App> apps = generateApps(appCount);
    if (appCount != apps.size()) {
      LOGGER.log(Level.WARNING, "Expected " + appCount + "app(s), created " + apps.size()
          + "app(s)...");
    }
  }

  private static List<App> generateApps(int howMany) {
    List<App> apps = new ArrayList<App>();
    final int random = DataGeneratorUtils.generateRandomInt(0, 3000);
    for (int i = 0; i < howMany; i++) {
      // init the app
      String appName = DataGeneratorUtils.getRandomName();
      App currentApp =
          new App(appName, DataGeneratorUtils.getRandomPlatform(),
              DataGeneratorUtils.getRandomIpsumBlock());
      // TODO: add the other 3 additional fields
      currentApp.setDownloadUrl("dl." + appName + ".org");
      currentApp.setLicense("License");
      currentApp.setLongDesccription("Long descr.");
      currentApp.setMinPlatformRequired("> 2.1");
      currentApp.setRating((double) DataGeneratorUtils.generateRandomInt(0, 10));
      currentApp.setSize(DataGeneratorUtils.generateRandomInt(0, 1000));
      currentApp.setSourceUrl("src_url");
      currentApp.setSupportUrl("supp_url");
      currentApp.setVersion(DataGeneratorUtils.generateRandomInt(1, 5) + ".9");
      // init the owner
      int userRandom = random + i;
      User currentUser =
          new User(new Long(userRandom), "username" + userRandom, "username" + userRandom
              + "@test.com");
      currentUser = facade.save(currentUser);
      currentApp.setCreator(currentUser);
      // add image pair artifact
      String[] pair = DataGeneratorUtils.getImagePair();
      currentApp.addArtifact(new Artifact("thumbnail", pair[0]));
      currentApp.addArtifact(new Artifact("image/png", pair[1]));
      // add tags
      currentApp.addTag(new Tag("tag" + DataGeneratorUtils.generateRandomInt(0, 5)));
      currentApp.addTag(new Tag("tag" + DataGeneratorUtils.generateRandomInt(0, 5)));
      currentApp = facade.save(currentApp);
      apps.add(currentApp);
    }
    return apps;
  }

  private static void dropExistingData() {
    facade.deleteAll(User.class);
    facade.deleteAll(App.class);
  }

}
