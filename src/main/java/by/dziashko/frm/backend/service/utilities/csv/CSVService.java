package by.dziashko.frm.backend.service.utilities.csv;

import by.dziashko.frm.backend.entity.material.Material;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class CSVService {

    public List<Material> processData(InputStream is){

        return CSVHandler.csvToMaterials(is);

    }
}
