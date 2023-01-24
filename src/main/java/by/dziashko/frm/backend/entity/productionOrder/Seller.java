package by.dziashko.frm.backend.entity.productionOrder;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Seller extends AbstractEntity implements Cloneable{

    private String name = "";

    private String lastName = "";

    private Integer phoneNumber;

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    private List<ProductionOrder> productionOrders = new LinkedList<>();

    public Seller() { }

    public Seller (String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductionOrder> getOrderNames() {
        return productionOrders;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
