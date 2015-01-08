package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@javax.persistence.Entity
@Table(name = "tag")
public class Tag implements Entity {
  private static final long serialVersionUID = 5120490367130075580L;
  @Id
  @GeneratedValue
  private Long id = 0L;
  private String value = "";
  @ManyToOne
  @JoinColumn(name = "app_id")
  @JsonIgnore
  App app = null;

  public Tag() {}

  public Tag(String value) {
    this.setValue(value);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
    if (!app.getTags().contains(this)) {
      app.getTags().add(this);
    }
  }

  public Long getId() {
    return id;
  }

  public void deleteId() {
    id = 0L;
  }
}
