package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "artifact")
public class Artifact implements Entity {
  private static final long serialVersionUID = -2840537551097305456L;
  @Id
  @GeneratedValue
  private Long id = 0L;
  private String type = null;
  private String url = null;
  private String description = null;
  @ManyToOne
  @JoinColumn(name = "app_id")
  private App belongingTo = null;

  public Artifact() {}

  public Artifact(String type, String url) {
    this.setType(type);
    this.setUrl(url);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public App getBelongingTo() {
    return belongingTo;
  }

  public void setBelongingTo(App belongingTo) {
    this.belongingTo = belongingTo;
    if (!belongingTo.getArtifacts().contains(this)) {
      belongingTo.getArtifacts().add(this);
    }
  }

  public Long getId() {
    return id;
  }
}
