package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * App Detail domain object. The contents of a certain detail are kept here with reference to the
 * App entity to which their belongs.
 * 
 * See also {@link AppDetailTypeEntity}.
 *
 */
@javax.persistence.Entity
@Table(name = "app_detail")
public class AppDetailEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  @ManyToOne
  @JoinColumn(name = "app_id")
  private AppEntity app = null;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "app_detail_type_id")
  private AppDetailTypeEntity type = null;
  private String contents = null;

  public AppDetailEntity() {}

  public AppDetailEntity(AppDetailTypeEntity forType, String contents) {
    this.type = forType;
    this.contents = contents;
  }

  public AppEntity getApp() {
    return app;
  }

  public void setApp(AppEntity app) {
    this.app = app;
    if (!this.app.getDetails().contains(this)) {
      this.app.getDetails().add(this);
    }
  }

  public AppDetailTypeEntity getType() {
    return type;
  }

  public void setType(AppDetailTypeEntity type) {
    this.type = type;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public int getId() {
    return id;
  }
}
