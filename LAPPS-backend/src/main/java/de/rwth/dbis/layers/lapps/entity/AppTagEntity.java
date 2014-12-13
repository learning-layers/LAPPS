package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@javax.persistence.Entity
@Table(name = "tag")
public class AppTagEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String name = null;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "app_instance_id")
  private AppInstanceEntity appInstance = null;

  public AppTagEntity() {}

  public AppTagEntity(String name) {
    this.name = name;
  }

  public AppTagEntity(String name, AppInstanceEntity appInstance) {
    this.name = name;
    this.appInstance = appInstance;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AppInstanceEntity getAppInstance() {
    return appInstance;
  }

  public void setAppInstance(AppInstanceEntity appInstance) {
    this.appInstance = appInstance;
    if (!this.appInstance.getTags().contains(this)) {
      appInstance.getTags().add(this);
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
        + ", for app: " + this.getAppInstance().getApp().getName();
  }
}
