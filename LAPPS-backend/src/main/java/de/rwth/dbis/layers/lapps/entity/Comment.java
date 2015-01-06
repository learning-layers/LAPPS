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
  private long id = 0;
  @Column(name = "comment_value")
  private String text = null;
  @OneToOne
  @JoinColumn(name = "user_id")
  User user = null;
  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "app_id")
  App app = null;
  @Temporal(TemporalType.TIMESTAMP)
  Date date = new Date();

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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", contents: "
        + this.getText() + ", from user: " + this.getUser().getEmail() + " regarding app: "
        + this.getApp().getName() + ", on: "
        + DateFormat.getInstance().format(this.getDate());
  }
}
