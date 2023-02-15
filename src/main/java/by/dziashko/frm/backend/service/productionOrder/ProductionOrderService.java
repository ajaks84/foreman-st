package by.dziashko.frm.backend.service.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.repo.productionOrder.ProductionOrderRepo;
import by.dziashko.frm.backend.repo.productionOrder.SellerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductionOrderService {
    private static final Logger LOGGER = Logger.getLogger(ProductionOrderService.class.getName());
    private final ProductionOrderRepo productionOrderRepo;
    private final SellerRepo sellerRepo;

    public ProductionOrderService(ProductionOrderRepo productionOrderRepo, SellerRepo sellerRepo) {
        this.productionOrderRepo = productionOrderRepo;
        this.sellerRepo = sellerRepo;
    }

    public List<ProductionOrder> findAll() {
        return productionOrderRepo.findAll();
    }

    public ProductionOrder find(Long id) {
        return productionOrderRepo.getById(id);
    }

    public ProductionOrder find(String s) {
        return productionOrderRepo.getByOrderNumber(s);
    }

    public List<ProductionOrder> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return productionOrderRepo.findAll();
        } else {
            return productionOrderRepo.search(stringFilter);
        }
    }

    public List<ProductionOrder> filterReady(Boolean value) {
        if (value == true) {
            return productionOrderRepo.getByOrderReadiness(ProductionOrder.Readiness.NotReady);
        } else {
            return productionOrderRepo.findAll();
        }
    }

    public List<ProductionOrder> filterReadyAndEmpty(Boolean value) {
        if (value == true) {
            return productionOrderRepo.getNotReadyAndEmptyReadiness();
        } else {
            return productionOrderRepo.findAll();
        }
    }

    public List<ProductionOrder> getNotReadyAndEmptyReadiness() {
        return productionOrderRepo.getNotReadyAndEmptyReadiness();
    }

    public long count() {
        return productionOrderRepo.count();
    }

    public void delete(ProductionOrder productionOrder) {
        productionOrderRepo.delete(productionOrder);
    }

    public void deleteAll() {
        productionOrderRepo.deleteAll();
    }

    public void save(ProductionOrder productionOrder) {
        if (productionOrder == null) {
            LOGGER.log(Level.SEVERE,
                    "ProductionOrder is null. Are you sure you have connected your form to the application?");
            return;
        }

        productionOrderRepo.save(productionOrder);
    }

}
