package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * App Platform domain object. Platforms are for example Android, iOS, Windows Phone x and others.
 *
 */
@javax.persistence.Entity
@Table(name = "app_platform")
public class AppPlatformEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String name = null;

  public AppPlatformEntity() {}

  public AppPlatformEntity(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.id + ", name: " + this.getName();
  }
}
