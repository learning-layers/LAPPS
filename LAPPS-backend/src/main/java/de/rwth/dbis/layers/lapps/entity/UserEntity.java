package de.rwth.dbis.layers.lapps.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User domain object.
 * 
 */
@javax.persistence.Entity
@Table(name = "user")
public class UserEntity implements Entity, Comparable<UserEntity> {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private Integer id = 0;
  @Column(name = "oidc_id")
  private long oidcId = 0;
  private String email = null;
  private String username = null;

  private int roles = 0;

  // TODO: remove this @JsonIgnore, we need it because their is a recursive loop between author
  // (user) <-> appcomment <-> author (user) <-> appcomment
  @JsonIgnore
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppCommentEntity> comments = new ArrayList<AppCommentEntity>();

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppInstanceRightsEntity> rights = new ArrayList<AppInstanceRightsEntity>();

  public UserEntity() {}

  public UserEntity(long oidcId, String email, String username) {
    this.setOidcId(oidcId);
    this.setEmail(email);
    this.setUsername(username);
  }

  public long getOidcId() {
    return oidcId;
  }

  public void setOidcId(long oidcId) {
    this.oidcId = oidcId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void addComment(AppCommentEntity c) {
    this.comments.add(c);
    if (c.getAuthor() != this) {
      c.setAuthor(this);
    }
  }

  public boolean hasRoles(int roles) {
    return (this.roles & roles) == roles;
  }

  public void grantRoles(int toGrant) {
    this.roles = this.roles | toGrant;
  }

  public void dropRoles(int toDrop) {
    this.roles = this.roles - (this.roles & toDrop);
  }


  public List<AppCommentEntity> getComments() {
    return comments;
  }

  public void addRights(AppInstanceRightsEntity rights) {
    this.rights.add(rights);
    if (rights.getUser() != this) {
      rights.setUser(this);
    }
  }

  public List<AppInstanceRightsEntity> getRights() {
    return this.rights;
  }



  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.id + ", oidcId: " + this.getOidcId()
        + ", email: " + this.getEmail();
  }

  // Default constructor
  @Override
  public int compareTo(UserEntity o) {
    if (this.getEmail() == null) {
      return -1;
    } else if (o.getEmail() == null) {
      return 1;
    } else {
      return this.getEmail().compareToIgnoreCase(o.getEmail());
    }
  }
}
