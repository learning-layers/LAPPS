package de.rwth.dbis.layers.lapps.entity;

public class AppInstanceOverview implements Entity {
  private static final long serialVersionUID = -355646226620364799L;
  private int id = 0;
  private String name = null;
  private double rating = 0;
  private String thumbnailUrl = null;
  private String shortDescription = null;
  private String platform = null;
  private String ownerEmail = null;

  public AppInstanceOverview(int id, String name, double rating, String thumbnailUrl,
      String shortDescription, String platform) {
    super();
    this.id = id;
    this.name = name;
    this.rating = rating;
    this.thumbnailUrl = thumbnailUrl;
    this.shortDescription = shortDescription;
    this.platform = platform;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getOwnerEmail() {
    return ownerEmail;
  }

  public void setOwnerEmail(String ownerEmail) {
    this.ownerEmail = ownerEmail;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "[" + this.getClass().getName() + "] id = " + this.getId();
  }
}
