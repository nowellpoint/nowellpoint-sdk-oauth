package com.redhat.sforce.qb.bean.model;

public class Product extends SObject {

	private static final long serialVersionUID = 1L;
	private String name;
    private String description;
    private String family;
    private String productCode;
    private String primaryBusinessUnit;
    private String productLine;
    private String unitOfMeasure;
    private Integer term;

    public Product() {
        super();
    }
	
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFamily() {
		return family;
	}
	
	public void setFamily(String family) {
		this.family = family;
	}
	
	public String getProductCode() {
		return productCode;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPrimaryBusinessUnit() {
		return primaryBusinessUnit;
	}

	public void setPrimaryBusinessUnit(String primaryBusinessUnit) {
		this.primaryBusinessUnit = primaryBusinessUnit;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}	
}