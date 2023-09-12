package by.dziashko.frm.backend.repo.invoiceItem;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepo extends JpaRepository<InvoiceItem, Long> {
    InvoiceItem getByEan(String s);
}
