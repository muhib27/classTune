package com.classtune.app.schoolapp.model;

import java.util.HashMap;
import java.util.List;

public class CmartProduct {

	private String id;
	private String name;
	private String entityType;
	private String shortDescription;
	private String description;
	private String link;
	private String icon;
	private String inStock;
	private String isSalable;
	private String hasGallery;
	private String hasOptions;
	private String minSaleQty;
	private String ratingSummary;
	private CmartProductPrice price;
	private HashMap<BaseType, List<BaseType>> attributes;
	
	public HashMap<BaseType, List<BaseType>> getAttributes() {
		return attributes;
	}
	public void setAttributes(
			HashMap<BaseType, List<BaseType>> attributes) {
		this.attributes = attributes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getInStock() {
		return inStock;
	}
	public void setInStock(String inStock) {
		this.inStock = inStock;
	}
	public String getIsSalable() {
		return isSalable;
	}
	public void setIsSalable(String isSalable) {
		this.isSalable = isSalable;
	}
	public String getHasGallery() {
		return hasGallery;
	}
	public void setHasGallery(String hasGallery) {
		this.hasGallery = hasGallery;
	}
	public String getHasOptions() {
		return hasOptions;
	}
	public void setHasOptions(String hasOptions) {
		this.hasOptions = hasOptions;
	}
	public String getMinSaleQty() {
		return minSaleQty;
	}
	public void setMinSaleQty(String minSaleQty) {
		this.minSaleQty = minSaleQty;
	}
	public String getRatingSummary() {
		return ratingSummary;
	}
	public void setRatingSummary(String ratingSummary) {
		this.ratingSummary = ratingSummary;
	}
	public CmartProductPrice getPrice() {
		return price;
	}
	public void setPrice(CmartProductPrice price) {
		this.price = price;
	}
	
	
}
