package de.rwth.dbis.layers.lapps.entity;

public class AppCommentEntity implements Entity {
  private static final long serialVersionUID = 1L;
  private int id = 0;
  private String text = null;
  UserEntity author = null;

  public AppCommentEntity() {}

  public AppCommentEntity(String text, UserEntity author) {
    this.setText(text);
    this.setAuthor(author);
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
  }

  public int getId() {
    return id;
  }
}
