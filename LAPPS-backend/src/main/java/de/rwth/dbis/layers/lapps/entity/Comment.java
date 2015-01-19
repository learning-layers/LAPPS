package de.rwth.dbis.layers.lapps.entity;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@javax.persistence.Entity
@Table(name = "comment")
public class Comment implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private Long id = 0L;
  private String content = null;
  @ManyToOne
  @JoinColumn(name = "user_id")
  User user = null;
  @ManyToOne
  @JoinColumn(name = "app_id")
  @JsonIgnore
  App app = null;
  private int rating = 0;
  @Temporal(TemporalType.TIMESTAMP)
  Date releaseDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lastUpdateDate")
  Date updateDate = new Date();

  public Comment() {}

  public Comment(String text, User user, App app) {
    this(text, 0, user, app);

  }

  public Comment(String text, int rating, User user, App app) {
    this.setContent(text);
    this.setUser(user);
    this.setApp(app);
    this.setRating(rating);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String text) {
    this.content = text;
  }

  public int getRating() {
    return this.rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public App getApp() {
    return this.app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date date) {
    this.releaseDate = date;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date date) {
    this.updateDate = date;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", contents: "
        + this.getContent() + ", from user: " + this.getUser().getEmail() + " regarding app: "
        + this.getApp().getName() + ", on: "
        + DateFormat.getInstance().format(this.getUpdateDate());
  }
}
