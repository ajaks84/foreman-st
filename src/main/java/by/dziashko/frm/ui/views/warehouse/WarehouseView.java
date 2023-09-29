package by.dziashko.frm.ui.views.warehouse;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.service.invoiceItem.InvoiceItemService;
import by.dziashko.frm.ui.forms.warehouse.WarehouseForm;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
import java.io.Serializable;

@Scope("prototype")
@Route(value = "warehouse", layout = MainView.class)
@CssImport("./styles/views/order/order-view.css")
@HtmlImport("/style.html")
public class WarehouseView extends VerticalLayout implements Serializable, LocaleChangeObserver {

    InvoiceItemService invoiceItemService;
    Grid<InvoiceItem> grid = new Grid<>(InvoiceItem.class);
    WarehouseForm form;
    Checkbox checkbox = new Checkbox();

    public WarehouseView(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Warehouse"));

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new WarehouseForm();

        Div content = new Div(grid,form);
        content.addClassName("content");
        content.setSizeFull();
        add(content);

        closeEditor();

    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(InvoiceItem::getArtNumber).setHeader(getTranslation("ArtNumber"));
        grid.addColumn(InvoiceItem::getDescription).setHeader(getTranslation("Description"));
        grid.addColumn(InvoiceItem::getEan).setHeader(getTranslation("EAN"));
        grid.addColumn(InvoiceItem::getNetPrice).setHeader(getTranslation("Net_Price"));
        grid.addColumn(InvoiceItem::getQuantity).setHeader(getTranslation("Quantity"));
        grid.addColumn(InvoiceItem -> setCorrectUoM(InvoiceItem.getUnitOfMeasure())).setHeader(getTranslation("UoM")).setKey("ordegridrCol");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.addItemClickListener(event -> navigateTo(event.getItem()));

        grid.setItems(invoiceItemService.findAll());


    }

    private String setCorrectUoM(String uom){
        return "pcs.";
    }

    private void closeEditor() {
//        form.setProductionOrder(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
