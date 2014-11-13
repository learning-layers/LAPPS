package de.rwth.dbis.layers.lapps.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
  private double rating = 0D;
  private String name = null;
  @OneToMany(mappedBy = "app")
  private List<AppCommentEntity> comments = new ArrayList<AppCommentEntity>();

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
