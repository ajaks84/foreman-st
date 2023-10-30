package by.dziashko.frm.ui.views.warehouse;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Hr;

public class InvoiceItemMenu extends GridContextMenu<InvoiceItem> {
    public InvoiceItemMenu(Grid<InvoiceItem> target) {
        super(target);

        addItem("Edit", e -> e.getItem().ifPresent(invoiceItem -> {
            // System.out.printf("Edit: %s%n", person.getFullName());
        }));
        addItem("Delete", e -> e.getItem().ifPresent(invoiceItem -> {
            // System.out.printf("Delete: %s%n", person.getFullName());
        }));

        add(new Hr());

        GridMenuItem<InvoiceItem> emailItem = addItem("Email",
                e -> e.getItem().ifPresent(invoiceItem -> {
                    // System.out.printf("Email: %s%n", person.getFullName());
                }));
        GridMenuItem<InvoiceItem> phoneItem = addItem("Call",
                e -> e.getItem().ifPresent(invoiceItem -> {
                    // System.out.printf("Phone: %s%n", person.getFullName());
                }));

        setDynamicContentHandler(invoiceItem -> {
            // Do not show context menu when header is clicked
            if (invoiceItem == null)
                return false;
            emailItem.setText(
                    String.format("Email: %s", invoiceItem.getArtNumber()));
            phoneItem.setText(String.format("Call: %s",
                    invoiceItem.getDescription()));
            return true;
        });
    }
}
