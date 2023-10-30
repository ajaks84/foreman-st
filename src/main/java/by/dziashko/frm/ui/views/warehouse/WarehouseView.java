package by.dziashko.frm.ui.views.warehouse;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.service.invoiceItem.InvoiceItemService;
import by.dziashko.frm.ui.forms.warehouse.WarehouseForm;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
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
    TextField filterText = new TextField();
    Checkbox checkbox = new Checkbox();
    ListDataProvider listDataProvider;
    ListDataProvider listDataProviderForm;
    private InvoiceItem draggedItem;

    public WarehouseView(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;

        listDataProvider = new ListDataProvider<>(invoiceItemService.findAll());

        form = new WarehouseForm(listDataProvider);
        form.addListener(WarehouseForm.CloseEvent.class, e -> closeEditor());
        this.listDataProviderForm = form.getDataProvider();

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Warehouse"));

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        Div content = new Div(grid,form);
        content.addClassName("content");
        content.setSizeFull();
        add(getToolbar(),content);

        closeEditor();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder(getTranslation("SearchByDesc"));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> searchInList());

        Button addOrderButton = new Button(getTranslation("CreateNewRecipe"));
        addOrderButton.addClickListener(click -> openEditor());

        Button addItemButton = new Button(getTranslation("Recipe_name"));
        addItemButton.addClickListener(click -> addItemToRecipe());
        addItemButton.addClickListener(click -> fireEvent(new WarehouseView.PushEvent(this)));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addOrderButton,addItemButton); //addOrderButton,checkbox,
        toolbar.addClassName("toolbar");
        return toolbar;
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
        grid.setDataProvider(getAllInvoiceItems());
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        // Drag & drop
        grid.setDropMode(GridDropMode.ON_GRID);
        grid.setRowsDraggable(true);
        grid.addDragStartListener(this::handleDragStart);
        grid.addDropListener(e -> {
            if (listDataProvider.getItems().contains(draggedItem)) {
                return;
            }
            listDataProviderForm.getItems().remove(draggedItem);
            listDataProviderForm.refreshAll();
            listDataProvider.getItems().add(draggedItem);
            listDataProvider.refreshAll();
        });
        grid.addDragEndListener(this::handleDragEnd);
    }

    private void handleDragStart(GridDragStartEvent<InvoiceItem> invoiceItemGridDragStartEvent) {
        draggedItem = invoiceItemGridDragStartEvent.getDraggedItems().get(0);
        System.out.println("handleDragStart "+draggedItem.getId());
    }

    private void handleDragEnd(GridDragEndEvent<InvoiceItem> invoiceItemGridDragEndEvent) {
        //draggedItem = invoiceItemGridDragEndEvent.
        System.out.println("handleDragEnd");
    }

    public ListDataProvider getDataProvider() {
        //grid.setItems(emptyRecipe.getItems());
        return listDataProvider;
    }

    private ListDataProvider<InvoiceItem> getAllInvoiceItems(){
        this.listDataProvider = new ListDataProvider<>(invoiceItemService.findAll());
        return listDataProvider;
    }

    private ListDataProvider<InvoiceItem> getFilteredInvoiceItems(){
        this.listDataProvider = new ListDataProvider<>(invoiceItemService.findAll(filterText.getValue()));
        return listDataProvider;
    }

    private String setCorrectUoM(String uom){
        return "pcs.";
    }

    private void closeEditor() {
//        form.setProductionOrder(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void searchInList() {

        grid.setDataProvider(getFilteredInvoiceItems());
        //grid.setItems(invoiceItemService.findAll(filterText.getValue()));
    }

    private void addItemToRecipe (){
        InvoiceItem item = new InvoiceItem();
        item.setArtNumber("art number");
        item.setFvDate("date");
        form.getEmptyRecipe().addItem(item);
        String val = String.valueOf(form.getEmptyRecipe().getItems().size());
        //fireEvent(new WarehouseView.PushEvent(this));
        System.out.println("Close EVENT "+val);
    }

    void openEditor() {
        grid.asSingleSelect().clear();
        form.setVisible(true);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }

    // Events
    public static abstract class WarehouseEvent extends ComponentEvent<WarehouseView> {

        protected WarehouseEvent(WarehouseView source) {
            super(source,false);
        }
    }

    public static class PushEvent extends WarehouseView.WarehouseEvent {
        PushEvent(WarehouseView source) {
            super(source);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
