package by.dziashko.frm.backend.entity.invoiceItem;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class InvoiceItem extends AbstractEntity implements Cloneable{

    private String ean = "";
    private String description = "";
    private String type = "";
    private String quantity = "";
    private String unitOfMeasure = "";
    private String netPrice = "";
    private String taxRate = "";
    private String categoryCode = "";
    private String taxAmount = "";
    private String netAmount = "";
    private String fvNumber = "";
    private String fvDate = "";
    private String artNumber = "";

    public InvoiceItem() {
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(String netPrice) {
        this.netPrice = netPrice;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getFvNumber() {
        return fvNumber;
    }

    public void setFvNumber(String fvNumber) {
        this.fvNumber = fvNumber;
    }

    public String getFvDate() {
        return fvDate;
    }

    public void setFvDate(String fvDate) {
        this.fvDate = fvDate;
    }

    public String getArtNumber() {
        return artNumber;
    }

    public void setArtNumber(String artNumber) {
        this.artNumber = artNumber;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "ean='" + ean + '\'' +
                ", description='" + description + '\'' +
                ", fvNumber='" + fvNumber + '\'' +
                '}';
    }
}
