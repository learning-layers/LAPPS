package de.rwth.dbis.layers.lapps.entity;

import java.util.List;

public class AppEntity implements Entity {
  private static final long serialVersionUID = 1L;
  private int id = 0;
  private double rating = 0D;
  private String name = null;
  private List<AppCommentEntity> comments = null;

  public AppEntity() {}

  public AppEntity(String name) {
    this.setName(name);
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
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

  public List<AppCommentEntity> getComments() {
    return comments;
  }
}
