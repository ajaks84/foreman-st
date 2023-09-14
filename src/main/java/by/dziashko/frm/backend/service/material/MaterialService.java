package by.dziashko.frm.backend.service.material;

import by.dziashko.frm.backend.entity.material.Material;
import by.dziashko.frm.backend.repo.material.MaterialRepo;
import by.dziashko.frm.backend.service.utilities.csv.CSVService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MaterialService {
    private static final Logger LOGGER = Logger.getLogger(MaterialService.class.getName());

    private final MaterialRepo materialRepo;
    private final CSVService csvService;

    public MaterialService(MaterialRepo materialRepo, CSVService csvService) {
        this.materialRepo = materialRepo;
        this.csvService = csvService;
    }

    public List<Material> findAll() {
        return materialRepo.findAll();
    }

    public Material find(String s) {
        return materialRepo.getByName(s);
    }

    public List<Material> getByEan(String ean) {
        return materialRepo.getByEan(ean);
    }

    public void save(List<Material> materials) {
        materialRepo.saveAll(materials);
    }

    public void delete(Material material) {
        materialRepo.delete(material);
    }

}
