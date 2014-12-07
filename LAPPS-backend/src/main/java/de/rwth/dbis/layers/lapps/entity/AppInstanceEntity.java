package de.rwth.dbis.layers.lapps.entity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * App Instance domain object. App instances are, for example, distributions of the same app on
 * different platforms, e.g. TurboApp for Android or TurboApp for iOS.
 * 
 */
@javax.persistence.Entity
@Table(name = "app_instance")
public class AppInstanceEntity implements Entity, Comparable<AppInstanceEntity> {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private int id = 0;
  @ManyToOne
  @JoinColumn(name = "app_id")
  private AppEntity app = null;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "platform_id")
  private AppPlatformEntity platform = null;
  private String url = null;
  private String version = null;
  private int size = 0;
  private String sourceUrl = null;

  @Column(name = "min_platform_version")
  private String availableOn = null;

  @Column(name = "date_created")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated = new Date();
  @Column(name = "date_modified")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateModified = null;

  @JsonIgnore
  @OneToMany(mappedBy = "appInstance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppDetailEntity> details = new ArrayList<AppDetailEntity>();
  @JsonIgnore
  @OneToMany(mappedBy = "appInstance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppArtifactEntity> artifacts = new ArrayList<AppArtifactEntity>();
  // @JsonIgnore
  @OneToMany(mappedBy = "appInstance", fetch = FetchType.EAGER)
  // Workaround concerning the duplication of child collection entries caused by outer join in
  // hibernate implementation
  @Fetch(FetchMode.SUBSELECT)
  private List<AppCommentEntity> comments = new ArrayList<AppCommentEntity>();
  @JsonIgnore
  @OneToMany(mappedBy = "appInstance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppTagEntity> tags = new ArrayList<AppTagEntity>();
  @JsonIgnore
  @OneToMany(mappedBy = "appInstance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AppInstanceRightsEntity> rights = new ArrayList<AppInstanceRightsEntity>();

  public AppInstanceEntity() {}

  public AppInstanceEntity(AppPlatformEntity onPlatform, String url) {
    this.platform = onPlatform;
    this.url = url;
  }

  public AppEntity getApp() {
    return app;
  }

  public void setApp(AppEntity app) {
    this.app = app;
    if (!app.getInstances().contains(this)) {
      app.getInstances().add(this);
    }
  }

  public int getId() {
    return id;
  }

  public AppPlatformEntity getPlatform() {
    return platform;
  }

  public void setPlatform(AppPlatformEntity platform) {
    this.platform = platform;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getAvailableOn() {
    return availableOn;
  }

  public void setAvailableOn(String availableOn) {
    this.availableOn = availableOn;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getDateModified() {
    return dateModified;
  }

  public void setDateModified(Date dateModified) {
    this.dateModified = dateModified;
  }

  public List<AppArtifactEntity> getArtifacts() {
    return artifacts;
  }

  public void addArtifacts(AppArtifactEntity artifact) {
    this.artifacts.add(artifact);
    if (artifact.getAppInstance() != this) {
      artifact.setAppInstance(this);
    }
  }

  public List<AppDetailEntity> getDetails() {
    return details;
  }

  public void addDetail(AppDetailEntity detail) {
    this.details.add(detail);
    if (detail.getAppInstance() != this) {
      detail.setAppInstance(this);
    }
  }

  public List<AppCommentEntity> getComments() {
    return comments;
  }

  public void addComment(AppCommentEntity comment) {
    this.comments.add(comment);
    if (comment.getAppInstance() != this) {
      comment.setAppInstance(this);
    }
  }

  public List<AppTagEntity> getTags() {
    return tags;
  }

  public void addTag(AppTagEntity tag) {
    this.tags.add(tag);
    if (tag.getAppInstance() != this) {
      tag.setAppInstance(this);
    }
  }

  public List<AppInstanceRightsEntity> getRights() {
    return rights;
  }

  public void addRights(AppInstanceRightsEntity rights) {
    this.rights.add(rights);
    if (rights.getAppInstance() != this) {
      rights.setAppInstance(this);
    }
  }

  @Override
  public String toString() {
    return "["
        + this.getClass().getName()
        + "] id = "
        + this.getId()
        + ", for app "
        + this.getApp().getName()
        + " (created on "
        + DateFormat.getInstance().format(this.getDateCreated())
        + ", last modified on "
        + (this.getDateModified() != null ? DateFormat.getInstance().format(this.getDateModified())
            : "never") + ") version " + this.getVersion() + ", on " + this.getPlatform().getName()
        + " [" + this.getSize() + "KB], available at " + this.getUrl() + ", with source at "
        + this.getSourceUrl() + " having " + this.getComments().size() + " comment(s), "
        + this.getArtifacts().size() + " artifact(s), " + this.getDetails().size()
        + " description(s) and " + this.getTags().size() + " tag(s)" + "availble for: "
        + this.getAvailableOn();
  }

  // Default comparator
  @Override
  public int compareTo(AppInstanceEntity o) {
    if (this.getApp().getName() == null) {
      return -1;
    } else if (o.getApp().getName() == null) {
      return 1;
    } else {
      return this.getApp().getName().compareToIgnoreCase(o.getApp().getName());
    }
  }
}
