package by.dziashko.frm.backend.service.cabin;

import by.dziashko.frm.backend.entity.cabin.CabinData;
import by.dziashko.frm.backend.repo.cabin.CabinDataRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CabinDataService {
    private static final Logger LOGGER = Logger.getLogger(CabinDataService.class.getName());

    private final CabinDataRepo cabinDataRepo;

    public CabinDataService(CabinDataRepo cabinDataRepo) {
        this.cabinDataRepo = cabinDataRepo;
    }

    public List<CabinData> findAll() {
        return cabinDataRepo.findAll();
    }

    public CabinData find(String s) {
        return cabinDataRepo.getByModelName(s);
    }

    public void save(CabinData cabinData) {
        if (cabinData == null) {
            LOGGER.log(Level.SEVERE,
                    "CabinData is null. Are you sure you have connected your form to the application?");
            return;
        }
        cabinDataRepo.save(cabinData);
    }

    public void delete(CabinData cabinData) {
        cabinDataRepo.delete(cabinData);
    }
}

