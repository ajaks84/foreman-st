package by.dziashko.frm.backend.service.invoiceItem;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.repo.invoiceItem.InvoiceItemRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class InvoiceItemService {
    private static final Logger LOGGER = Logger.getLogger(InvoiceItemService.class.getName());

    private final InvoiceItemRepo invoiceItemRepo;

    public InvoiceItemService(InvoiceItemRepo invoiceItemRepo) {
        this.invoiceItemRepo = invoiceItemRepo;
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

    public void deleteAll() {
        invoiceItemRepo.deleteAll();
        LOGGER.info("Invoice item DB has been cleaned");
    }
}
