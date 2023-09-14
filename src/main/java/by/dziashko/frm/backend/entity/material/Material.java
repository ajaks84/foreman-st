package by.dziashko.frm.backend.entity.material;

import by.dziashko.frm.backend.entity.AbstractEntity;
import javax.persistence.Entity;

@Entity
public class Material extends AbstractEntity implements Cloneable{

    private String number = "";
    private String name = "";
    private String description = "";
    private String quantity = "";
    private String ean = "";

    public Material() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    @Override
    public String toString() {
        return "Material{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", EAN='" + ean + '\'' +
                '}';
    }
}
