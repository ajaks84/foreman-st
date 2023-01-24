package by.dziashko.frm.backend.service.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorData;
import by.dziashko.frm.backend.repo.aspirator.AspiratorDataRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AspiratorDataService {
    private static final Logger LOGGER = Logger.getLogger(AspiratorDataService.class.getName());

    private AspiratorDataRepo aspiratorDataRepo;

    public AspiratorDataService(AspiratorDataRepo aspiratorDataRepo) {
        this.aspiratorDataRepo = aspiratorDataRepo;
    }

    public List<AspiratorData> findAll() {
        return aspiratorDataRepo.findAll();
    }

    public AspiratorData find(String s) {
        return aspiratorDataRepo.getByModelName(s);
    }

    public void save(AspiratorData aspiratorData) {
        if (aspiratorData == null) {
            LOGGER.log(Level.SEVERE,
                    "AspiratorData is null. Are you sure you have connected your form to the application?");
            return;
        }
        aspiratorDataRepo.save(aspiratorData);
    }

    public void delete(AspiratorData aspiratorData) {
        aspiratorDataRepo.delete(aspiratorData);
    }
}
