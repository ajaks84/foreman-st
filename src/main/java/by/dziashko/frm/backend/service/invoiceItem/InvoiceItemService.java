package by.dziashko.frm.backend.service.invoiceItem;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.entity.material.Material;
import by.dziashko.frm.backend.repo.invoiceItem.InvoiceItemRepo;
import by.dziashko.frm.backend.repo.material.MaterialRepo;
import by.dziashko.frm.backend.service.material.MaterialService;
import by.dziashko.frm.backend.service.utilities.csv.CSVService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class InvoiceItemService {
    private static final Logger LOGGER = Logger.getLogger(InvoiceItemService.class.getName());

    private final InvoiceItemRepo invoiceItemRepo;
    private final CSVService csvService; // change to XML Service

    public InvoiceItemService(InvoiceItemRepo invoiceItemRepo, CSVService csvService) {
        this.invoiceItemRepo = invoiceItemRepo;
        this.csvService = csvService;
    }

    public List<InvoiceItem> findAll() {
        return invoiceItemRepo.findAll();
    }

    public InvoiceItem find(String s) {
        return invoiceItemRepo.getByEan(s);
    }

    public void save(List<InvoiceItem> invoiceItems) {
        invoiceItemRepo.saveAll(invoiceItems);
    }

    public void delete(InvoiceItem invoiceItem) {
        invoiceItemRepo.delete(invoiceItem);
    }
}
