package de.rwth.dbis.layers.lapps.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@javax.persistence.Entity
@Table(name = "user")
public class User implements Entity, Comparable<User> {
  private static final long serialVersionUID = -1687031374202240517L;
  @Id
  @GeneratedValue
  @JsonIgnore
  private Long id = 0L;
  @Column(name = "oidc_id")
  private Long oidcId = null;
  private String email = null;
  private String username = null;
  private Integer role = 0;

  public static final Integer DEVELOPER = 10;
  public static final Integer ADMIN = 20;

  @Column(name = "date_registered")
  private Date dateRegistered = new Date();
  private String description = null;
  private String website = null;


  @OneToMany(mappedBy = "creator")
  private List<App> apps = new ArrayList<App>();

  public User() {}

  public User(Long oidcId, String username, String email) {
    this.setOidcId(oidcId);
    this.setUsername(username);
    this.setEmail(email);
  }

  public Long getOidcId() {
    return oidcId;
  }

  public void setOidcId(Long oidcId) {
    this.oidcId = oidcId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public Date getDateRegistered() {
    return dateRegistered;
  }

  public void setDateRegistered(Date dateRegistered) {
    this.dateRegistered = dateRegistered;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }



  @Override
  public int compareTo(User o) {
    if (this.getEmail() == null) {
      return -1;
    } else if (o.getEmail() == null) {
      return 1;
    } else {
      return this.getEmail().compareToIgnoreCase(o.getEmail());
    }
  }
}
