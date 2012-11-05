package com.sfa.qb.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Table(name="PricebookEntry")

public class PricebookEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
	
	@Column(name="SalesforceId", length=20, unique=true)
	private String salesforceId;
	
	@Column(name="CurrencyIsoCode", length=3)
	private String currencyIsoCode;
	
	@Column(name="UnitPrice")
	private Double unitPrice;
	
	@Column(name="IsActive")
	private Boolean isActive;
	
	@Column(name="IsDeleted")
	private Boolean isDeleted;
	
	@OneToOne
    @JoinColumn(name = "ProductId", referencedColumnName = "Id")
	private Product product;
	
	@OneToOne
    @JoinColumn(name = "PricebookId", referencedColumnName = "Id")
	private Pricebook pricebook;
	
	public PricebookEntry() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSalesforceId() {
		return salesforceId;
	}

	public void setSalesforceId(String salesforceId) {
		this.salesforceId = salesforceId;
	}

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Boolean getIsDeleted() {
		return this.isDeleted;
	}
	
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Pricebook getPricebook() {
		return pricebook;
	}
	
	public void setPricebook(Pricebook pricebook) {
		this.pricebook = pricebook;
	}
	
	@Override
	public String toString() {
	    return "Id: " + getId() +
	    		" CurrencyIsoCode: " + getCurrencyIsoCode() +
	    		" UnitPrice: " + getUnitPrice() +
	    		" Product (" + product.toString() + ")";
	}
}