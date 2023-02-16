package by.dziashko.frm.backend.entity.cyclone;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class CycloneData extends AbstractEntity implements Cloneable{

    private String modelName = "";

    public CycloneData() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
