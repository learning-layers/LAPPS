package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * App Instance domain object. App instances are, for example, distributions of the same app on
 * different platforms, e.g. TurboApp for Android or TurboApp for iOS.
 *
 */
@javax.persistence.Entity
@Table(name = "app_instance")
public class AppInstanceEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "app_id")
  private AppEntity app = null;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "platform_id")
  private AppPlatformEntity platform = null;
  private String url = null;

  public AppInstanceEntity() {}

  public AppInstanceEntity(AppPlatformEntity onPlatform, String url) {
    this.platform = onPlatform;
    this.url = url;
  }

  public AppEntity getApp() {
    return app;
  }

  public void setApp(AppEntity app) {
    this.app = app;
    if (!app.getInstances().contains(this)) {
      app.getInstances().add(this);
    }
  }

  public int getId() {
    return id;
  }

  public AppPlatformEntity getPlatform() {
    return platform;
  }

  public void setPlatform(AppPlatformEntity platform) {
    this.platform = platform;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", for app: "
        + this.getApp().getName() + ", on: " + this.getPlatform().getName() + ", available at: "
        + this.getUrl();
  }
}
