package by.dziashko.frm.backend.service.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorOption;
import by.dziashko.frm.backend.repo.aspirator.AspiratorOptionRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AspiratorOptionService {
    private static final Logger LOGGER = Logger.getLogger(AspiratorOptionService.class.getName());

    private AspiratorOptionRepo aspiratorOptionRepo;

    public AspiratorOptionService(AspiratorOptionRepo aspiratorOptionRepo) {
        this.aspiratorOptionRepo = aspiratorOptionRepo;
    }

    public List<AspiratorOption> findAll() {
        return aspiratorOptionRepo.findAll();
    }

    public AspiratorOption find(String s) {
        return aspiratorOptionRepo.getByModelName(s);
    }

    public void save(AspiratorOption entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Entry is null. Are you sure you have connected your form to the application?");
            return;
        }
        aspiratorOptionRepo.save(entry);
    }

    public void delete(AspiratorOption entry) {
        aspiratorOptionRepo.delete(entry);
    }
}
