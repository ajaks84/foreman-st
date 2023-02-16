package by.dziashko.frm.backend.service.addOptions;

import by.dziashko.frm.backend.entity.addOptions.AdditionalOptions;
import by.dziashko.frm.backend.repo.addOptions.AdditionalOptionsRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AdditionalOptionsService {
    private static final Logger LOGGER = Logger.getLogger(AdditionalOptionsService.class.getName());

    private final AdditionalOptionsRepo additionalOptionsRepo;

    public AdditionalOptionsService(AdditionalOptionsRepo additionalOptionsRepo) {
        this.additionalOptionsRepo = additionalOptionsRepo;
    }

    public List<AdditionalOptions> findAll() {
        return additionalOptionsRepo.findAll();
    }

    public AdditionalOptions find(String s) {
        return additionalOptionsRepo.getByModelName(s);
    }

    public void save(AdditionalOptions additionalOptions) {
        if (additionalOptions == null) {
            LOGGER.log(Level.SEVERE,
                    "AdditionalOptions is null. Are you sure you have connected your form to the application?");
            return;
        }
        additionalOptionsRepo.save(additionalOptions);
    }

    public void delete(AdditionalOptions additionalOptions) {
        additionalOptionsRepo.delete(additionalOptions);
    }
}

