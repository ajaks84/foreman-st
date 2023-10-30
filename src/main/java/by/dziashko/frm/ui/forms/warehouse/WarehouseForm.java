package by.dziashko.frm.ui.forms.warehouse;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.recipe.Recipe;
import by.dziashko.frm.ui.forms.productionOrder.ProductionOrderForm;
import by.dziashko.frm.ui.views.warehouse.WarehouseView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class WarehouseForm extends VerticalLayout {
    TextField text = new TextField(getTranslation("Recipe_name"));
    Grid<InvoiceItem> grid = new Grid<>(InvoiceItem.class);
    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));
    Recipe emptyRecipe = new Recipe();
    ListDataProvider listDataProvider;
    ListDataProvider listDataProviderView;
    private InvoiceItem draggedItem;

    public WarehouseForm(ListDataProvider listDataProviderView) {
        this.setWidth("50%");
        this.addListener(WarehouseView.PushEvent.class, pushEvent -> someReaction()); //Not working
        this.listDataProviderView = listDataProviderView;
        HorizontalLayout hL_1 = new HorizontalLayout(text);
        HorizontalLayout hL_3 = new HorizontalLayout(createButtonsLayout());

        add(hL_1, configureGrid(), hL_3);

    }

    private ListDataProvider<InvoiceItem> createDataProvider(){
        InvoiceItem item = new InvoiceItem();
        item.setArtNumber("Art number");
        item.setDescription("Description");
        item.setNetAmount("9.99zł");
        emptyRecipe.getItems().add(item);
        listDataProvider = new ListDataProvider<>(emptyRecipe.getItems());
        return listDataProvider;
    }

    private void someReaction(){
        System.out.println("some reaction");
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        //save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new WarehouseForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    private Component configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(InvoiceItem::getArtNumber).setHeader("ArtNumber");
        grid.addColumn(InvoiceItem::getDescription).setHeader("Description");
        grid.addColumn(InvoiceItem::getQuantity).setHeader(getTranslation("Quantity"));
        grid.setDataProvider(createDataProvider());
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
            listDataProviderView.getItems().remove(draggedItem);
            listDataProviderView.refreshAll();
            listDataProvider.getItems().add(draggedItem);
            listDataProvider.refreshAll();
        });
        grid.addDragEndListener(this::handleDragEnd);
        return grid;
    }

    private void handleDragStart(GridDragStartEvent<InvoiceItem> invoiceItemGridDragStartEvent) {
        System.out.println("handleDragStart");
    }

    private void handleDragEnd(GridDragEndEvent<InvoiceItem> invoiceItemGridDragEndEvent) {
        System.out.println("handleDragEnd");
    }

    public Recipe getEmptyRecipe() {
        grid.setItems(emptyRecipe.getItems());
        return emptyRecipe;
    }

    public ListDataProvider getDataProvider() {
        //grid.setItems(emptyRecipe.getItems());
        return listDataProvider;
    }

    // Events
    public static abstract class WarehouseFormEvent extends ComponentEvent<WarehouseForm> {
        private final InvoiceItem invoiceItem;

        protected WarehouseFormEvent(WarehouseForm source, InvoiceItem invoiceItem) {
            super(source, false);
            this.invoiceItem = invoiceItem;
        }

        public InvoiceItem getInvoiceItem() {
            return invoiceItem;
        }
    }

    public static class SaveEvent extends WarehouseForm.WarehouseFormEvent {
        SaveEvent(WarehouseForm source, InvoiceItem invoiceItem) {
            super(source, invoiceItem);
        }
    }

    public static class DeleteEvent extends WarehouseForm.WarehouseFormEvent {
        DeleteEvent(WarehouseForm source, InvoiceItem invoiceItem) {
            super(source, invoiceItem);
        }

    }

    public static class CloseEvent extends WarehouseForm.WarehouseFormEvent {
        CloseEvent(WarehouseForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

