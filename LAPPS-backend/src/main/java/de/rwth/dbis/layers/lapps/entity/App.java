package de.rwth.dbis.layers.lapps.entity;

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
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@javax.persistence.Entity
@Table(name = "app")
public class App implements Entity, Comparable<App> {
  private static final long serialVersionUID = -5148238127147716369L;
  @Id
  @GeneratedValue
  private Long id = 0L;
  private String name = null;
  private String platform = null;
  @Column(name = "supported_platform_version")
  private String minPlatformRequired = null;
  @Column(name = "download_url")
  private String downloadUrl = null;
  private String version = null;
  private Integer size = 0;
  @Column(name = "source_url")
  private String sourceUrl = null;
  @Column(name = "support_url")
  private String supportUrl = null;
  private Double rating = 0D;
  @Column(name = "date_created")
  private Date dateCreated = new Date();
  @Column(name = "date_modified")
  private Date dateModified = null;
  private String license = null;
  @Column(name = "short_description")
  private String shortDescription = null;
  @Column(name = "long_description")
  private String longDescription = null;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User creator = null;
  @OneToMany(mappedBy = "belongingTo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  List<Artifact> artifacts = new ArrayList<Artifact>();
  @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  // Hibernate-specific annotation is not the best way to go, but it is needed as a workaround
  // 'around' the issue about left outer join, performed by the framework on load operation. See
  // https://doctorjw.wordpress.com/2012/01/11/hibernate-collections-and-duplicate-objects/
  @Fetch(FetchMode.SUBSELECT)
  List<Tag> tags = new ArrayList<Tag>();

  /**
   * Empty constructor needed for JPA
   */
  public App() {}

  public App(String name, String platform, String shortDescription) {
    this.setName(name);
    this.setPlatform(platform);
    this.setShortDescription(shortDescription);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getMinPlatformRequired() {
    return minPlatformRequired;
  }

  public void setMinPlatformRequired(String minPlatformRequired) {
    this.minPlatformRequired = minPlatformRequired;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getSupportUrl() {
    return supportUrl;
  }

  public void setSupportUrl(String supportUrl) {
    this.supportUrl = supportUrl;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
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

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public List<Artifact> getArtifacts() {
    return artifacts;
  }

  public void addArtifact(Artifact artifact) {
    this.artifacts.add(artifact);
    if (artifact.getBelongingTo() != this) {
      artifact.setBelongingTo(this);
    }
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void addTag(Tag tag) {
    this.tags.add(tag);
    if (tag.getApp() != this) {
      tag.setApp(this);
    }
  }

  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (obj instanceof App) {
      return ((App) obj).getId().equals(this.getId());
    } else {
      return false;
    }
  }

  @Override
  public int compareTo(App o) {
    if (this.getName() == null) {
      return -1;
    } else if (o.getName() == null) {
      return 1;
    } else {
      return this.getName().compareToIgnoreCase(o.getName());
    }
  }
}
