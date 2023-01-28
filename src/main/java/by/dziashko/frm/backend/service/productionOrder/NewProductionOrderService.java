package by.dziashko.frm.backend.service.productionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.repo.newProductionOrder.NewProductionOrderRepo;
import by.dziashko.frm.backend.repo.newProductionOrder.ResponsiblePersonRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NewProductionOrderService {
    private static final Logger LOGGER = Logger.getLogger(NewProductionOrderService.class.getName());
    private final NewProductionOrderRepo newProductionOrderRepo;
    private final ResponsiblePersonRepo responsiblePersonRepo;

    public NewProductionOrderService(NewProductionOrderRepo newProductionOrderRepo, ResponsiblePersonRepo responsiblePersonRepo) {
        this.newProductionOrderRepo = newProductionOrderRepo;
        this.responsiblePersonRepo = responsiblePersonRepo;
    }

    public List<NewProductionOrder> findAll() {
        return newProductionOrderRepo.findAll();
    }

    public NewProductionOrder find(Long id) {
        return newProductionOrderRepo.getById(id);
    }

//    public NewProductionOrder find(String s) {
//        return newProductionOrderRepo.getByOrderNumber(s);
//    }

//    public List<NewProductionOrder> findAll(String stringFilter) {
//        if (stringFilter == null || stringFilter.isEmpty()) {
//            return newProductionOrderRepo.findAll();
//        } else {
//            return newProductionOrderRepo.search(stringFilter);
//        }
//    }

//    public List<NewProductionOrder> filterReady(Boolean value) {
//        if (value == true) {
//            return newProductionOrderRepo.getByOrderReadiness(NewProductionOrder.OrderStatus.Nie_gotowe);
//        } else {
//            return newProductionOrderRepo.findAll();
//        }
//    }

//    public List<NewProductionOrder> filterReadyAndEmpty(Boolean value) {
//        if (value == true) {
//            return newProductionOrderRepo.getNotReadyAndEmptyReadiness();
//        } else {
//            return newProductionOrderRepo.findAll();
//        }
//    }

//    public List<NewProductionOrder> getNotReadyAndEmptyReadiness() {
//        return newProductionOrderRepo.getNotReadyAndEmptyReadiness();
//    }

    public long count() {
        return newProductionOrderRepo.count();
    }

    public void delete(NewProductionOrder newProductionOrder) {
        newProductionOrderRepo.delete(newProductionOrder);
    }

    public void deleteAll() {
        newProductionOrderRepo.deleteAll();
    }

    public void save(NewProductionOrder newProductionOrder) {
        if (newProductionOrder == null) {
            LOGGER.log(Level.SEVERE,
                    "NewProductionOrder is null. Are you sure you've connected your form to the application?");
            return;
        }

        newProductionOrderRepo.save(newProductionOrder);
    }

}
