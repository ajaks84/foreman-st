package by.dziashko.frm.backend.service.cyclone;

import by.dziashko.frm.backend.entity.cyclone.CycloneData;
import by.dziashko.frm.backend.repo.cyclone.CycloneDataRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CycloneDataService {
    private static final Logger LOGGER = Logger.getLogger(CycloneDataService.class.getName());

    private final CycloneDataRepo cycloneDataRepo;

    public CycloneDataService(CycloneDataRepo cycloneDataRepo) {
        this.cycloneDataRepo = cycloneDataRepo;
    }

    public List<CycloneData> findAll() {
        return cycloneDataRepo.findAll();
    }

    public CycloneData find(String s) {
        return cycloneDataRepo.getByModelName(s);
    }

    public void save(CycloneData cycloneData) {
        if (cycloneData == null) {
            LOGGER.log(Level.SEVERE,
                    "CycloneData is null. Are you sure you have connected your form to the application?");
            return;
        }
        cycloneDataRepo.save(cycloneData);
    }

    public void delete(CycloneData cycloneData) {
        cycloneDataRepo.delete(cycloneData);
    }
}

