package by.dziashko.frm.ui.views.service;

import by.dziashko.frm.backend.service.invoiceItem.InvoiceItemService;
import by.dziashko.frm.backend.service.material.MaterialService;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.backend.service.utilities.GoogleSheetsReaderService;
import by.dziashko.frm.backend.service.utilities.csv.CSVService;
import by.dziashko.frm.backend.service.utilities.xml.XMLService;
import by.dziashko.frm.security.SecuredByRole;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@SecuredByRole("ROLE_Admin")
@Route(value = "service", layout = MainView.class)
@PageTitle("Service")
@CssImport("./styles/views/about/about-view.css")
public class ServiceView extends HorizontalLayout {

    private final CSVService csvService;
    private final MaterialService materialService;
    private final InvoiceItemService invoiceItemService;
    private final XMLService xmlService;

    public ServiceView(GoogleSheetsReaderService googleSheetsReaderService,
                       ProductionOrderService productionOrderService,
                       SellerService sellerService,
                       CSVService csvService,
                       MaterialService materialService,
                       InvoiceItemService invoiceItemService,
                       XMLService xmlService, InvoiceItemService invoiceItemService1, XMLService xmlService1) {

        setId("about-view");

        this.invoiceItemService = invoiceItemService1;
        this.xmlService = xmlService1;
        this.csvService = csvService;
        this.materialService = materialService;

        Button get = new Button("Get actual data");
        get.addClickListener(event -> {
            try {
                googleSheetsReaderService.getSheetsData();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        });

        Button clearPO = new Button("Clear production orders DB");
        clearPO.addClickListener(event -> productionOrderService.deleteAll());

        Button clearSL = new Button("Clear seller DB");
        clearSL.addClickListener(event -> sellerService.deleteAll());

        MemoryBuffer memoryBufferCSV = new MemoryBuffer();
        Upload csvFileUpload = new Upload(memoryBufferCSV);

        csvFileUpload.addSucceededListener(event -> {
            // Get information about the uploaded file
            InputStream fileData = memoryBufferCSV.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            // Do something with the file data
             processCSVFile(fileData, fileName, contentLength, mimeType);
        });

        MemoryBuffer memoryBufferXML = new MemoryBuffer();
        Upload xmlFileUpload = new Upload(memoryBufferXML);

        xmlFileUpload.addSucceededListener(event -> {
            // Get information about the uploaded file
            InputStream fileData = memoryBufferXML.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            // Do something with the file data
            try {
                processXMLFile(fileData, fileName, contentLength, mimeType);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        });

        add (get, clearPO, clearSL, csvFileUpload, xmlFileUpload);
    }

    private void processCSVFile(InputStream fileData, String fileName, long contentLength, String mimeType) {

        materialService.save(csvService.processData(fileData));

    }

    private void processXMLFile(InputStream fileData, String fileName, long contentLength, String mimeType) throws ParserConfigurationException, IOException, SAXException {

        invoiceItemService.save(xmlService.processData(fileData));

    }


}
