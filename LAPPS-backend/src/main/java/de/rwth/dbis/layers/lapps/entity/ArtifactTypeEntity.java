package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * App Artifact Type object. Images, videos etc.
 *
 */
@javax.persistence.Entity
@Table(name = "app_artifact_type")
public class ArtifactTypeEntity implements Entity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  private String type = null;

  public ArtifactTypeEntity() {}

  public ArtifactTypeEntity(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.id + ", type: " + this.getType();
  }

}
