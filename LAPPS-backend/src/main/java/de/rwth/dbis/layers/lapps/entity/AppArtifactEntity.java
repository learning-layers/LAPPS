package de.rwth.dbis.layers.lapps.entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * App artifact object. The media content of an app
 *
 */
@javax.persistence.Entity
@Table(name = "app_artifact")
public class AppArtifactEntity implements Entity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "app_instance_id")
  private AppInstanceEntity appInstance = null;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "app_artifact_type_id")
  private ArtifactTypeEntity artifactType = null;

  private String url = null;

  public AppArtifactEntity() {}

  public AppArtifactEntity(ArtifactTypeEntity withArtifact, String url) {
    this.artifactType = withArtifact;
    this.url = url;
  }

  public AppInstanceEntity getAppInstance() {
    return appInstance;
  }

  public void setAppInstance(AppInstanceEntity appInstance) {
    this.appInstance = appInstance;
    if (!appInstance.getArtifacts().contains(this)) {
      appInstance.getArtifacts().add(this);
    }
  }

  public int getId() {
    return id;
  }

  public ArtifactTypeEntity getArtifact() {
    return artifactType;
  }

  public void setArtifact(ArtifactTypeEntity artifact) {
    this.artifactType = artifact;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id: " + this.getId() + ", for platform: "
        + this.getAppInstance().getPlatform().getName() + ", with: " + this.getArtifact()
        + ", available at: " + this.getUrl();
  }

}
