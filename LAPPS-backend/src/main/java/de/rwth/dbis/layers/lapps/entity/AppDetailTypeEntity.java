package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * App Detail Type domain object. Different detail types are for example 'General description',
 * 'Installation instructions', etc..
 *
 */
@javax.persistence.Entity
@Table(name = "app_detail_type")
public class AppDetailTypeEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String type = null;

  public AppDetailTypeEntity() {}

  public AppDetailTypeEntity(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", type: " + this.getType();
  }
}
