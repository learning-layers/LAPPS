package de.rwth.dbis.layers.lapps.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * App domain object.
 *
 */
@javax.persistence.Entity
@Table(name = "app")
public class AppEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String name = null;
  @JsonIgnore
  @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppInstanceEntity> instances = new ArrayList<AppInstanceEntity>();


  public AppEntity() {}

  public AppEntity(String name) {
    this.setName(name);
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

  public List<AppInstanceEntity> getInstances() {
    return instances;
  }

  public void addInstance(AppInstanceEntity instance) {
    this.instances.add(instance);
    if (instance.getApp() != this) {
      instance.setApp(this);
    }
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", name: " + this.getName();
  }
}
