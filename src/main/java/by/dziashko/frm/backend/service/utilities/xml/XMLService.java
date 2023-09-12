package by.dziashko.frm.backend.service.utilities.xml;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class XMLService {

    public List<InvoiceItem> processData(InputStream is) throws ParserConfigurationException, IOException, SAXException {

        return XMLHandler.parseXMLDocument(is);

    }
}
