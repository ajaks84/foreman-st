package by.dziashko.frm.backend.entity.aspirator;

import by.dziashko.frm.backend.entity.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class AspiratorBody extends AbstractEntity implements Cloneable{

    private String modelName = "";

//    @OneToMany(mappedBy = "aspiratorBody", fetch = FetchType.EAGER)
//    private List<AspiratorData> aspiratorDataList = new LinkedList<>();

    public AspiratorBody() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
