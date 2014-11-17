package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "tag")
public class AppTagEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String name = null;
  @ManyToOne
  @JoinColumn(name = "app_id")
  private AppEntity app = null;

  public AppTagEntity() {}

  public AppTagEntity(String name) {
    this.name = name;
  }

  public AppTagEntity(String name, AppEntity app) {
    this.name = name;
    this.app = app;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AppEntity getApp() {
    return app;
  }

  public void setApp(AppEntity app) {
    this.app = app;
    if (!this.app.getTags().contains(this)) {
      app.getTags().add(this);
    }
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", name: " + this.getName()
        + ", for app: " + this.getApp().getName();
  }
}
