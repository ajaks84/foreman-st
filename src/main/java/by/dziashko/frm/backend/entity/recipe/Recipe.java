package by.dziashko.frm.backend.entity.recipe;

import by.dziashko.frm.backend.entity.AbstractEntity;
import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.entity.material.Material;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;


public class Recipe extends AbstractEntity implements Cloneable{
    List<InvoiceItem> items = new ArrayList<>();

    public Recipe() {
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public void addItem(InvoiceItem invoiceItem){
        items.add(invoiceItem);
    }

}
