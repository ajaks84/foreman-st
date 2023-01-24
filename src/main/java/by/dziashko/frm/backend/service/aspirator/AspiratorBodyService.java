package by.dziashko.frm.backend.service.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorBody;
import by.dziashko.frm.backend.repo.aspirator.AspiratorBodyRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AspiratorBodyService {
    private static final Logger LOGGER = Logger.getLogger(AspiratorBodyService.class.getName());

    private AspiratorBodyRepo aspiratorBodyRepo;

    public AspiratorBodyService(AspiratorBodyRepo aspiratorBodyRepo) {
        this.aspiratorBodyRepo = aspiratorBodyRepo;
    }

    public List<AspiratorBody> findAll() {
        return aspiratorBodyRepo.findAll();
    }

    public AspiratorBody find(String s) {
        return aspiratorBodyRepo.getByModelName(s);
    }

    public void save(AspiratorBody entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Entry is null. Are you sure you have connected your form to the application?");
            return;
        }
        aspiratorBodyRepo.save(entry);
    }

    public void delete(AspiratorBody entry) {
        aspiratorBodyRepo.delete(entry);
    }
}
