package by.dziashko.frm.backend.entity.cabin;

import by.dziashko.frm.backend.entity.AbstractEntity;
import javax.persistence.Entity;

@Entity
public class CabinData extends AbstractEntity implements Cloneable{

    private String modelName = "";

    public CabinData() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
