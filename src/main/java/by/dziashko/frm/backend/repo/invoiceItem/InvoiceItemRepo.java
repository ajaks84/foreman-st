package by.dziashko.frm.backend.repo.invoiceItem;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InvoiceItemRepo extends JpaRepository<InvoiceItem, Long> {

    InvoiceItem getByEan(String s);

    @Query("select c from InvoiceItem c where lower(c.description) like lower(concat('%', :searchTerm, '%'))")
    List<InvoiceItem> search(@Param("searchTerm") String searchTerm);
}
