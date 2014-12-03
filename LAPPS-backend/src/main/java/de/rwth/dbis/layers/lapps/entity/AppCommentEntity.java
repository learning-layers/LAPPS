package de.rwth.dbis.layers.lapps.entity;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Comment domain object.
 * 
 *
 */
@javax.persistence.Entity
@Table(name = "comment")
public class AppCommentEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String text = null;
  @ManyToOne
  @JoinColumn(name = "user_id")
  UserEntity author = null;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "app_instance_id")
  AppInstanceEntity appInstance = null;
  @Temporal(TemporalType.TIMESTAMP)
  Date date = new Date();

  public AppCommentEntity() {}

  public AppCommentEntity(String text, UserEntity author, AppInstanceEntity app) {
    this.setText(text);
    this.setAuthor(author);
    this.setAppInstance(app);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public UserEntity getAuthor() {
    return author;
  }

  public void setAuthor(UserEntity author) {
    this.author = author;
    if (!author.getComments().contains(this)) {
      author.getComments().add(this);
    }
  }

  public AppInstanceEntity getAppInstance() {
    return appInstance;
  }

  public void setAppInstance(AppInstanceEntity appInstance) {
    this.appInstance = appInstance;
    if (!appInstance.getComments().contains(this)) {
      appInstance.getComments().add(this);
    }
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", contents: "
        + this.getText() + ", from user: " + this.getAuthor().getEmail() + " regarding app: "
        + this.getAppInstance().getApp().getName() + ", on: "
        + DateFormat.getInstance().format(this.getDate());
  }
}
