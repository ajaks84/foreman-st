package by.dziashko.frm.backend.service.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorFan;
import by.dziashko.frm.backend.repo.aspirator.AspiratorFanRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AspiratorFanService {
    private static final Logger LOGGER = Logger.getLogger(AspiratorFanService.class.getName());

    private AspiratorFanRepo aspiratorFanRepo;

    public AspiratorFanService(AspiratorFanRepo aspiratorFanRepo) {
        this.aspiratorFanRepo = aspiratorFanRepo;
    }

    public List<AspiratorFan> findAll() {
        return aspiratorFanRepo.findAll();
    }

    public AspiratorFan find(String s) {
        return aspiratorFanRepo.getByModelName(s);
    }

    public void save(AspiratorFan aspiratorFan) {
        if (aspiratorFan == null) {
            LOGGER.log(Level.SEVERE,
                    "FanMotorData is null. Are you sure you have connected your form to the application?");
            return;
        }
        aspiratorFanRepo.save(aspiratorFan);
    }

    public void delete(AspiratorFan aspiratorFan) {
        aspiratorFanRepo.delete(aspiratorFan);
    }
}
