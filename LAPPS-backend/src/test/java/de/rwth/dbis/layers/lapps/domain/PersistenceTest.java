package de.rwth.dbis.layers.lapps.domain;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.DataGeneratorUtils;
import de.rwth.dbis.layers.lapps.DataGeneratorUtils.RandomNumberGenerator;
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.Artifact;
import de.rwth.dbis.layers.lapps.entity.Tag;
import de.rwth.dbis.layers.lapps.entity.User;

public class PersistenceTest {
  private static Facade facade = new Facade();
  private User user = null;
  private App app = null;

  @Before
  public void initData() {
    // generate and save data:
    this.user = facade.save(DataGeneratorUtils.getRandomDeveloperUser());
    App app = new App("Test App", "platform", "Lorem ipsum...");
    app.setCreator(this.user);
    app.addArtifact(new Artifact("image", RandomNumberGenerator.getRandomInt(0, 3000)
        + "@image.com"));
    app.addArtifact(new Artifact("image", RandomNumberGenerator.getRandomInt(0, 3000)
        + "@image.com"));
    app.addTag(new Tag("tag_" + RandomNumberGenerator.getRandomInt(0, 3000)));
    app.addTag(new Tag("tag_" + RandomNumberGenerator.getRandomInt(0, 3000)));
    this.app = facade.save(app);
  }

  @Test
  public void runTests() {
    this.allSaved();
    App a = this.loaded();
    this.modified(a);
  }

  private void allSaved() {
    Assert.assertTrue("An app is not persisted: " + this.app, this.app.getId() > 0);
    Assert.assertTrue("A user is not persisted: " + this.user, this.user.getId() > 0);
    Assert.assertTrue("This app (" + this.app + ") has no artifacts! :@", this.app.getArtifacts()
        .size() > 0);
    Assert.assertTrue("This app (" + this.app + ") has no tags! :@", this.app.getTags().size() > 0);
  }

  private App loaded() {
    // load data:
    User u = facade.load(User.class, this.user.getId());
    Assert.assertEquals("Wrong user loaded!", this.user.getId(), u.getId());
    App a = facade.load(App.class, this.app.getId());
    Assert.assertEquals("Wrong app loaded!", this.app.getId(), a.getId());
    Assert.assertEquals("Creator of the loaded app is different than the saved user!",
        this.user.getId(), a.getCreator().getId());
    return a;
  }

  private void modified(App a) {
    // modify/update data:
    a.setName("modified");
    a.addArtifact(new Artifact("image", RandomNumberGenerator.getRandomInt(0, 3000) + "@image.com"));
    a.addTag(new Tag("tag_" + RandomNumberGenerator.getRandomInt(0, 3000)));
    a = facade.save(a);
    Assert.assertNotSame("Both apps still have the same names!", a.getName(), this.app.getName());
    Assert.assertEquals("Unexpected artifact count! Check your collection loading strategy!", 3, a
        .getArtifacts().size());
    Assert.assertEquals("Unexpected tag count! Check your collection loading strategy!", 3, a
        .getTags().size());
  }

  @After
  public void dropData() {
    // delete data:
    facade.delete(App.class, this.app.getId());
    facade.delete(User.class, this.user.getId());
  }
}
