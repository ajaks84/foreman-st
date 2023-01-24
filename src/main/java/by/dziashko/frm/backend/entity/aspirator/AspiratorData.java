package by.dziashko.frm.backend.entity.aspirator;

import by.dziashko.frm.backend.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class AspiratorData extends AbstractEntity implements Cloneable {

    private String modelName = "";

    @ManyToOne
    @JoinColumn(name = "aspiratorBody_id")
    private AspiratorBody aspiratorBody;

    @ManyToOne
    @JoinColumn(name = "aspiratorFan_id")
    private AspiratorFan aspiratorFan;

    @ManyToOne
    @JoinColumn(name = "aspiratorElectric_id")
    private AspiratorElectric aspiratorElectric;

    @ManyToOne
    @JoinColumn(name = "aspiratorOption_id")
    private AspiratorOption aspiratorOption;

    public AspiratorData() {
    }

    public AspiratorData(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public AspiratorBody getAspiratorBody() {
        return aspiratorBody;
    }

    public void setAspiratorBody(AspiratorBody aspiratorBody) {
        this.aspiratorBody = aspiratorBody;
    }

    public AspiratorFan getAspiratorFan() {
        return aspiratorFan;
    }

    public void setAspiratorFan(AspiratorFan aspiratorFan) {
        this.aspiratorFan = aspiratorFan;
    }

    public AspiratorElectric getAspiratorElectric() {
        return aspiratorElectric;
    }

    public void setAspiratorElectric(AspiratorElectric aspiratorElectric) {
        this.aspiratorElectric = aspiratorElectric;
    }

    public AspiratorOption getAspiratorOption() {
        return aspiratorOption;
    }

    public void setAspiratorOption(AspiratorOption aspiratorOption) {
        this.aspiratorOption = aspiratorOption;
    }

}
