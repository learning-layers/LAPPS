package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Utils;
import de.rwth.dbis.layers.lapps.entity.AppArtifactEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailTypeEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
import de.rwth.dbis.layers.lapps.entity.AppTagEntity;
import de.rwth.dbis.layers.lapps.entity.ArtifactTypeEntity;

public class AppInstanceFacadeTest {
  private static AppInstanceFacade appInstanceFacade = AppInstanceFacade.getFacade();
  private static AppFacade appFacade = AppFacade.getFacade();
  private static String appInstanceUrl = "http://instanceTest.com";
  private static String appName = "TestInstanceApp";

  private AppInstanceEntity appInstance = null;
  private List<AppPlatformEntity> platforms = AppPlatformFacade.getFacade().findAll();
  private List<AppDetailTypeEntity> detailTypes = AppDetailTypeFacade.getFacade().findAll();
  private List<ArtifactTypeEntity> artifactTypes = ArtifactFacade.getFacade().findAll();

  @Before
  public void init() {
    assertTrue("No platforms!", platforms.size() > 0);
    assertTrue("No Detail types!", detailTypes.size() > 0);
    assertTrue("No Artifact types!", artifactTypes.size() > 0);
    AppEntity app = new AppEntity(appName);
    appInstance =
        new AppInstanceEntity(platforms.get(Utils.generateRandomInt(0, platforms.size())),
            appInstanceUrl);
    appInstance.setApp(app);
    appInstance = appFacade.save(app).getInstances().get(0);
  }

  @Test
  public void doTests() {
    this.testAppConsistency();
    this.addAppInstance();
  }

  @After
  public void deleteExisting() {
    appFacade.deleteAll("name", appName);
  }

  private void addAppInstance() {
    AppInstanceEntity secondAppInstance =
        new AppInstanceEntity(platforms.get(Utils.generateRandomInt(0, platforms.size())),
            "http://anotherTestInstance.com");
    secondAppInstance.addArtifacts(new AppArtifactEntity(artifactTypes.get(Utils.generateRandomInt(
        0, artifactTypes.size())), "http://appInstanceTestArtifact.com"));
    secondAppInstance.addDetail(new AppDetailEntity(detailTypes.get(Utils.generateRandomInt(0,
        detailTypes.size())), "Some App Instance Test description"));
    secondAppInstance.addTag(new AppTagEntity("appInstanceTestTag"));
    secondAppInstance.setApp(appInstance.getApp());
    secondAppInstance = appInstanceFacade.save(secondAppInstance);
    assertTrue("Second app instance has not been saved!", secondAppInstance.getId() > 0);
  }

  private void testAppConsistency() {
    assertTrue("App Instance is null!", appInstance != null);
    assertTrue("App Instance not saved!", appInstance.getId() > 0);
    assertTrue("App Instance does not have an app!", appInstance.getApp() != null);
    assertTrue("App Instance did not save its app properly!", appInstance.getApp().getId() > 0);
  }
}
