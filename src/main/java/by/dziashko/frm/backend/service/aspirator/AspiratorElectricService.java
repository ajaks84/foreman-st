package by.dziashko.frm.backend.service.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorElectric;
import by.dziashko.frm.backend.entity.aspirator.AspiratorOption;
import by.dziashko.frm.backend.repo.aspirator.AspiratorElectricRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AspiratorElectricService {
    private static final Logger LOGGER = Logger.getLogger(AspiratorElectricService.class.getName());

    private AspiratorElectricRepo aspiratorElectricRepo;

    public AspiratorElectricService(AspiratorElectricRepo aspiratorElectricRepo) {
        this.aspiratorElectricRepo = aspiratorElectricRepo;
    }

    public List<AspiratorElectric> findAll() {
        return aspiratorElectricRepo.findAll();
    }

    public AspiratorElectric find(String s) {
        return aspiratorElectricRepo.getByModelName(s);
    }

    public void save(AspiratorElectric entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Entry is null. Are you sure you have connected your form to the application?");
            return;
        }
        aspiratorElectricRepo.save(entry);
    }

    public void delete(AspiratorElectric entry) {
        aspiratorElectricRepo.delete(entry);
    }
}
