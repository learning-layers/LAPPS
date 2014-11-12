package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "user")
public class UserEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  private Integer id = 0;
  @Column(name = "oidc_id")
  private Integer oidcId = 0;
  private String email = null;

  public UserEntity() {}

  public UserEntity(Integer oidcId, String email) {
    this.setOidcId(oidcId);
    this.setEmail(email);
  }

  public Integer getOidcId() {
    return oidcId;
  }

  public void setOidcId(Integer oidcId) {
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
}
