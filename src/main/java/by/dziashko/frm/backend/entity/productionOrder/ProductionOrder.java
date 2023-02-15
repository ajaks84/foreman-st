package by.dziashko.frm.backend.entity.productionOrder;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class ProductionOrder extends AbstractEntity implements Cloneable {

    public enum Readiness { Ready("Gotowe"), NotReady("Nie gotowe"), Sent("Wysłane");

        private final String readiness;
        Readiness(String readiness) {
            this.readiness = readiness;
        }

        public String getReadiness(){
            return readiness;
        }
    }

    //1.
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    //2.1
    @NotNull
    @NotEmpty
    private String projectNumber = "";

    //2.
    @NotNull
    @NotEmpty
    private String client = "";
    //3.
    @NotNull
    @NotEmpty
    private String orderNumber = "";
    //4.
    @NotNull
    @NotEmpty
    private String orderDate;
    //5.
    @NotNull
    @NotEmpty
    private String orderDeadLine = "";
    //6.
    @NotNull
    @NotEmpty
    private String orderSendDate = "";
    //7.
    @NotNull
    @NotEmpty
    private String orderDelay = "";
    //8.
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductionOrder.Readiness orderReadiness;
    //9.
    @NotNull
    @NotEmpty
    private String cabinType = "";
    //10.
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductionOrder.Readiness cabinReadiness;
    //11.
    @NotNull
    @NotEmpty
    private String aspiratorType = "";
    //12.
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductionOrder.Readiness aspiratorReadiness;
    //13.
    @NotNull
    @NotEmpty
    private String separatorType = "";
    //14.
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductionOrder.Readiness separatorReadiness;
    //15.
    @NotNull
    @NotEmpty
    @Column(length = 2000)
    private String additionalOptions = "";
    //16.
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductionOrder.Readiness additionalOptionsReadiness;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDeadLine() {
        return orderDeadLine;
    }

    public void setOrderDeadLine(String orderDeadLine) {
        this.orderDeadLine = orderDeadLine;
    }

    public String getOrderSendDate() {
        return orderSendDate;
    }

    public void setOrderSendDate(String orderSendDate) {
        this.orderSendDate = orderSendDate;
    }

    public String getOrderDelay() {
        return orderDelay;
    }

    public void setOrderDelay(String orderDelay) {
        this.orderDelay = orderDelay;
    }

    public Readiness getOrderReadiness() {
        return orderReadiness;
    }

    public void setOrderReadiness(Readiness orderReadiness) {
        this.orderReadiness = orderReadiness;
    }

    public Readiness getCabinReadiness() {
        return cabinReadiness;
    }

    public void setCabinReadiness(Readiness cabinReadiness) {
        this.cabinReadiness = cabinReadiness;
    }

    public String getAspiratorType() {
        return aspiratorType;
    }

    public void setAspiratorType(String aspiratorType) {
        this.aspiratorType = aspiratorType;
    }

    public Readiness getAspiratorReadiness() {
        return aspiratorReadiness;
    }

    public void setAspiratorReadiness(Readiness aspiratorReadiness) {
        this.aspiratorReadiness = aspiratorReadiness;
    }

    public String getSeparatorType() {
        return separatorType;
    }

    public void setSeparatorType(String separatorType) {
        this.separatorType = separatorType;
    }

    public Readiness getSeparatorReadiness() {
        return separatorReadiness;
    }

    public void setSeparatorReadiness(Readiness separatorReadiness) {
        this.separatorReadiness = separatorReadiness;
    }

    @Column(length = 2000)
    public String getAdditionalOptions() {
        return additionalOptions;
    }

    public void setAdditionalOptions(String additionalOptions) {
        this.additionalOptions = additionalOptions;
    }

    public Readiness getAdditionalOptionsReadiness() {
        return additionalOptionsReadiness;
    }

    public void setAdditionalOptionsReadiness(Readiness additionalOptionsReadiness) {
        this.additionalOptionsReadiness = additionalOptionsReadiness;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

}