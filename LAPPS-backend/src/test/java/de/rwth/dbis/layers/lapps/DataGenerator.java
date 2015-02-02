package de.rwth.dbis.layers.lapps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import de.rwth.dbis.layers.lapps.DataGeneratorUtils.RandomNumberGenerator;
import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.Artifact;
import de.rwth.dbis.layers.lapps.entity.Comment;
import de.rwth.dbis.layers.lapps.entity.Tag;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Helper class to generate dummy data.
 */
public class DataGenerator {
  private static Logger LOGGER = Logger.getLogger(DataGenerator.class.getName());
  private static Facade facade = new Facade();
  private static int DEFAULT_APP_COUNT = 100;
  private static int appCount = DEFAULT_APP_COUNT;


  /**
   * Generates some dummy data based on the count of apps requested.
   * 
   * @param args - the number of apps requested
   */
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
    String[] userNames =
        new String[] {"Malcolm Reynolds", "Zoe Washburne", "Hoban Washburne", "Inara Serra",
            "Jayne Cobb", "Kaylee Frye"};
    User[] users = new User[userNames.length];
    for (int i = 0; i < userNames.length; i++) {

      String name = userNames[i];
      long oidcId = name.hashCode();
      String email = name.replace(" ", "").toLowerCase() + "@test.foobar";
      String website = "http://" + name.replace(" ", "") + ".something";
      String description = "This is the personal description of " + name + "!";
      User user = new User(oidcId, name, email, description, website, User.DEVELOPER);

      user = facade.save(user);
      users[i] = user;
    }

    for (int i = 0; i < howMany; i++) {

      // initialize the app
      App currentApp =
          new App(DataGeneratorUtils.getRandomName(), DataGeneratorUtils.getRandomPlatform(),
              DataGeneratorUtils.getRandomShortDescription(),
              DataGeneratorUtils.getRandomVersion(), DataGeneratorUtils.getRandomLongDescription(),
              DataGeneratorUtils.getRandomUrl());
      currentApp.setLicense("Copyright 2014");// check here for DB restrictions?
      currentApp.setRating(DataGeneratorUtils.getRandomRating());
      currentApp.setSize(DataGeneratorUtils.getRandomSize());
      currentApp.setSourceUrl(DataGeneratorUtils.getRandomUrl());
      currentApp.setSupportUrl(DataGeneratorUtils.getRandomUrl());
      currentApp.setMinPlatformRequired(DataGeneratorUtils.getRandomMinPlatform());

      User currentUser = users[RandomNumberGenerator.getRandomInt(0, users.length - 1)];
      currentApp.setCreator(currentUser);

      DataGeneratorUtils.getRandomThumbnailUrl();
      Artifact thumbnail = new Artifact("thumbnail", DataGeneratorUtils.getRandomThumbnailUrl());

      thumbnail.setDescription(DataGeneratorUtils.getRandomImageDescription());// please check for
                                                                               // max length in DB ?

      currentApp.addArtifact(thumbnail);

      // add video
      Artifact vida = new Artifact("video/youtube", DataGeneratorUtils.getRandomVideoUrl());
      vida.setDescription(DataGeneratorUtils.getRandomImageDescription());
      currentApp.addArtifact(vida);


      // add images
      String[] images = DataGeneratorUtils.getRandomImageUrls(4);
      for (int j = 0; j < images.length; j++) {
        Artifact imga = new Artifact("image/png", images[j]);
        imga.setDescription(DataGeneratorUtils.getRandomImageDescription());
        currentApp.addArtifact(imga);
      }

      String[] tags = DataGeneratorUtils.getRandomTags(2, 10);

      for (int j = 0; j < tags.length; j++) {
        currentApp.addTag(new Tag(tags[j]));
      }

      currentApp = facade.save(currentApp);

      List<Comment> comments =
          DataGeneratorUtils.getRandomComments(0, 22, currentApp,
              new ArrayList<User>(Arrays.asList(users)));

      for (Comment comment : comments) {
        facade.save(comment);
      }

      apps.add(currentApp);
    }
    return apps;
  }

  private static void dropExistingData() {
    facade.deleteAll(Comment.class);
    facade.deleteAll(User.class);
    facade.deleteAll(App.class);
  }

  /**
   * This method runs the mockup data class as a test.
   * 
   * Use "mvn test -Dtest=DataGenerator" to generate mockup data in the database.
   */
  @Test
  public void generate() {
    DataGenerator.main(new String[] {"100"});
  }
}
