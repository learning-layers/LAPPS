package de.rwth.dbis.layers.lapps.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
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
  @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppCommentEntity> comments = new ArrayList<AppCommentEntity>();
  @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppTagEntity> tags = new ArrayList<AppTagEntity>();
  @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppInstanceEntity> instances = new ArrayList<AppInstanceEntity>();

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

  public void addComment(AppCommentEntity comment) {
    this.comments.add(comment);
    if (comment.getApp() != this) {
      comment.setApp(this);
    }
  }

  public List<AppTagEntity> getTags() {
    return tags;
  }

  public void addTag(AppTagEntity tag) {
    this.tags.add(tag);
    if (tag.getApp() != this) {
      tag.setApp(this);
    }
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
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", name: " + this.getName()
        + ", rating: " + this.getRating() + ", comments: " + this.getComments().size() + ", tags: "
        + this.getTags().size();
  }
}
