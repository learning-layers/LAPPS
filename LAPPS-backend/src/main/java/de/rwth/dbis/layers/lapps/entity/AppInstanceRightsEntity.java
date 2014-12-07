package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "app_instance_management")
public class AppInstanceRightsEntity implements Entity {
  private static final long serialVersionUID = 719666222722429917L;
  public static final int CREATE = 1 << 0;

  @Id
  @GeneratedValue
  private int id = 0;
  private int rights = 0;

  @ManyToOne
  @JoinColumn(name = "user_id")
  UserEntity user = null;

  @ManyToOne
  @JoinColumn(name = "app_instance_id")
  AppInstanceEntity appInstance = null;


  public AppInstanceRightsEntity() {}

  public AppInstanceRightsEntity(int rights, UserEntity user, AppInstanceEntity appInstance) {
    this.setRights(rights);
    this.setUser(user);
    this.setAppInstance(appInstance);
  }

  public int getRights() {
    return rights;
  }

  public void setRights(int rights) {
    this.rights = rights;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
    if (!user.getRights().contains(this)) {
      user.getRights().add(this);
    }
  }

  public AppInstanceEntity getAppInstance() {
    return appInstance;
  }

  public void setAppInstance(AppInstanceEntity appInstance) {
    this.appInstance = appInstance;
    if (!appInstance.getRights().contains(this)) {
      appInstance.getRights().add(this);
    }
  }


  public int getId() {
    return id;
  }

  public boolean hasRights(int rights) {
    return (this.rights & rights) == rights;
  }

  public void grantRights(int toGrant) {
    this.rights = this.rights | toGrant;
  }

  public void dropRights(int toDrop) {
    this.rights = this.rights - (this.rights & toDrop);
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", from user: "
        + this.getUser().getEmail() + " regarding app: " + this.getAppInstance().getPlatform()
        + ", rigths: " + this.getRights();
  }
}
