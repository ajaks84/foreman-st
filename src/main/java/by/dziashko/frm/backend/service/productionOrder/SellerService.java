package by.dziashko.frm.backend.service.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.repo.productionOrder.SellerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SellerService {

    private static final Logger LOGGER = Logger.getLogger(SellerService.class.getName());
    private final SellerRepo sellerRepo;

    public SellerService(SellerRepo sellerRepo) {
        this.sellerRepo = sellerRepo;
    }

    public List<Seller> findAll() {
        return sellerRepo.findAll();
    }

    public Seller find(String s) {
        return sellerRepo.getByName(s);
    }

    public void save(Seller seller) {
        if (seller == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        sellerRepo.save(seller);
    }

    public void delete(Seller seller) {
        sellerRepo.delete(seller);
    }

    public void deleteAll(){
        sellerRepo.deleteAll();
    }

    public Seller getByName(String name) {
        return sellerRepo.getByName(name);
    }

}