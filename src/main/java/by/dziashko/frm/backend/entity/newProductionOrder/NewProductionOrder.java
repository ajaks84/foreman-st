package by.dziashko.frm.backend.entity.newProductionOrder;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class NewProductionOrder extends AbstractEntity implements Cloneable {

    //public enum OrderStatus {Gotowe, Nie_gotowe, Wysłane}  // You better find out how to i18N enums....

    public enum OrderStatus { Status_zlecenia, Nowe, Gotowe, Nie_gotowe, Wysłane }

    //public enum Readiness {Ready, NotReady, Sent}

    //1.
    @NotNull
    @NotEmpty
    private String client = "";

    //2.
    @NotNull
    @NotEmpty
    private String projectNumber = "";

    //3.
    @ManyToOne
    @JoinColumn(name = "responsiblePerson_id")
    private ResponsiblePerson responsiblePerson;

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
    private String orderDelay = "";

    //7.
    @NotNull
    @NotEmpty
    private String planedDispatchDate = "";

    //8.
    @NotNull
    @NotEmpty
    private String planedOrderCompletionDate = "";

    //9.
    @NotNull
    @NotEmpty
    private String termsOfDelivery = "";

    //10.
    @Enumerated(EnumType.STRING)
    @NotNull
    private NewProductionOrder.OrderStatus orderStatus;

    //11.
    @NotNull
    @NotEmpty
    private String info = "";

    //12.
    @NotNull
    @NotEmpty
    private String orderDetailsRef = "";

    //13.
    @NotNull
    @NotEmpty
    private String orderBomRef = "";


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

    public String getPlanedDispatchDate() {
        return planedDispatchDate;
    }

    public void setPlanedDispatchDate(String planedDispatchDate) {
        this.planedDispatchDate = planedDispatchDate;
    }

    public String getOrderDelay() {
        return orderDelay;
    }

    public void setOrderDelay(String orderDelay) {
        this.orderDelay = orderDelay;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderOrderStatus) {
        this.orderStatus = orderOrderStatus;
    }

    public ResponsiblePerson getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(ResponsiblePerson responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getPlanedOrderCompletionDate() {
        return planedOrderCompletionDate;
    }

    public void setPlanedOrderCompletionDate(String planedOrderCompletionDate) {
        this.planedOrderCompletionDate = planedOrderCompletionDate;
    }

    public String getTermsOfDelivery() {
        return termsOfDelivery;
    }

    public void setTermsOfDelivery(String termsOfDelivery) {
        this.termsOfDelivery = termsOfDelivery;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOrderDetailsRef() {
        return orderDetailsRef;
    }

    public void setOrderDetailsRef(String orderDetailsRef) {
        this.orderDetailsRef = orderDetailsRef;
    }

    public String getOrderBomRef() {
        return orderBomRef;
    }

    public void setOrderBomRef(String orderBomRef) {
        this.orderBomRef = orderBomRef;
    }

}