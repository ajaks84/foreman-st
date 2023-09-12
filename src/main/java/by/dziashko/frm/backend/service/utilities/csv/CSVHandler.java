package by.dziashko.frm.backend.service.utilities.csv;

import by.dziashko.frm.backend.entity.material.Material;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVHandler {

    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static List<Material> csvToMaterials(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withDelimiter(';')))
        {

            List<Material> materials = new ArrayList<Material>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Material material = new Material();
                material.setNumber(csvRecord.get(0));
                material.setQuantity(csvRecord.get(1));
                material.setDescription(csvRecord.get(2));
                material.setName(csvRecord.get(3));
                material.setUnitPrice(csvRecord.get(4));

                materials.add(material);
            }

            return materials;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }


}
