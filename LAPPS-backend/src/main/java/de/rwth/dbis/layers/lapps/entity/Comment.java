package de.rwth.dbis.layers.lapps.entity;

import java.text.DateFormat;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.OneToOne;
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
  @Column(name = "content")
  private String text = null;
  @OneToOne
  @JoinColumn(name = "authorId")
  User author = null;
  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "appId")
  App app = null;
  @Column(name = "rating")
  private int rating = 0;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "releaseDate")
  Date releaseDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lastUpdateDate")
  Date updateDate = new Date();

  public Comment() {}

  public Comment(String text, User user, App app) {
    this.setText(text);
    this.setUser(user);
    this.setApp(app);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getRating() {
    return this.rating;
  }
  
  public void setRating(int rating) {
    this.rating = rating;
  }
  
  public User getUser() {
    return this.author;
  }

  public void setUser(User user) {
    this.author = user;    
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
        + this.getText() + ", from user: " + this.getUser().getEmail() + " regarding app: "
        + this.getApp().getName() + ", on: "
        + DateFormat.getInstance().format(this.getUpdateDate());
  }
}
