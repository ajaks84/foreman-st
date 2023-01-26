package by.dziashko.frm.backend.service.productionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.repo.newProductionOrder.ResponsiblePersonRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ResponsiblePersonService {

    private static final Logger LOGGER = Logger.getLogger(ResponsiblePersonService.class.getName());
    private final ResponsiblePersonRepo responsiblePersonRepo;

    public ResponsiblePersonService(ResponsiblePersonRepo responsiblePersonRepo) {
        this.responsiblePersonRepo = responsiblePersonRepo;
    }

    public List<ResponsiblePerson> findAll() {
        return responsiblePersonRepo.findAll();
    }

    public ResponsiblePerson find(String s) {
        return responsiblePersonRepo.getByName(s);
    }

    public void save(ResponsiblePerson responsiblePerson) {
        if (responsiblePerson == null) {
            LOGGER.log(Level.SEVERE,
                    "ResponsiblePerson is null. Are you sure you've connected your form to the application?");
            return;
        }
        responsiblePersonRepo.save(responsiblePerson);
    }

    public void delete(ResponsiblePerson responsiblePerson) {
        responsiblePersonRepo.delete(responsiblePerson);
    }

    public void deleteAll(){
        responsiblePersonRepo.deleteAll();
    }

    public ResponsiblePerson getByName(String name) {
        return responsiblePersonRepo.getByName(name);
    }

}