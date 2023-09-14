package by.dziashko.frm.backend.service.utilities.xml;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.service.material.MaterialService;
import by.dziashko.frm.backend.service.utilities.csv.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {

    public static List<InvoiceItem> parseXMLDocument(InputStream is) throws ParserConfigurationException, IOException, SAXException {

        List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();
        Node fvNumberNode = doc.getElementsByTagName("InvoiceNumber").item(0);
        String fvNumber = fvNumberNode.getFirstChild().getNodeValue();
        Node fvDateNode = doc.getElementsByTagName("InvoiceDate").item(0);
        String fvDate = fvDateNode.getFirstChild().getNodeValue();
        int numberOfInvoiceLines = doc.getElementsByTagName("Line-Item").getLength();

        for (int i = 0; i < numberOfInvoiceLines; i++) {
            Node node = doc.getElementsByTagName("Line-Item").item(i);
            NodeList nodeList = node.getChildNodes();
            int n = nodeList.getLength();
            Node current;

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setFvNumber(fvNumber);
            invoiceItem.setFvDate(fvDate);
            for (int y = 0; y < n; y++) {
                current = nodeList.item(y);
                if (current.getNodeType() == Node.ELEMENT_NODE) {
                    if (current.getNodeName()=="EAN"){
                        invoiceItem.setEan(current.getTextContent());

                    }
                    if (current.getNodeName()=="ItemDescription"){invoiceItem.setDescription(current.getTextContent());}
                    if (current.getNodeName()=="ItemType"){invoiceItem.setType(current.getTextContent());}
                    if (current.getNodeName()=="InvoiceQuantity"){invoiceItem.setQuantity(current.getTextContent());}
                    if (current.getNodeName()=="UnitOfMeasure"){invoiceItem.setUnitOfMeasure(current.getTextContent());}
                    if (current.getNodeName()=="InvoiceUnitNetPrice"){invoiceItem.setNetPrice(current.getTextContent());}
                    if (current.getNodeName()=="TaxRate"){invoiceItem.setTaxRate(current.getTextContent());}
                    if (current.getNodeName()=="TaxCategoryCode"){invoiceItem.setCategoryCode(current.getTextContent());}
                    if (current.getNodeName()=="TaxAmount"){invoiceItem.setTaxAmount(current.getTextContent());}
                    if (current.getNodeName()=="NetAmount"){invoiceItem.setNetAmount(current.getTextContent());}



                    invoiceItems.add(invoiceItem);
                }

            }
        }

            return invoiceItems;
        }
    }
