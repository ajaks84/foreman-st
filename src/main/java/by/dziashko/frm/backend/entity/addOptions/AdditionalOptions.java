package by.dziashko.frm.backend.entity.addOptions;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class AdditionalOptions extends AbstractEntity implements Cloneable{

    private String modelName = "";

    public AdditionalOptions() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
