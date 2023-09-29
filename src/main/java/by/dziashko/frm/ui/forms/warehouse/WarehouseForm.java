package by.dziashko.frm.ui.forms.warehouse;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class WarehouseForm extends FormLayout {
    Text text = new Text(getTranslation("Seller"));
    Grid<InvoiceItem> grid = new Grid<>(InvoiceItem.class);
    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    public WarehouseForm() {
        addClassName("contact-form");

        grid.setItems();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout_1 = new VerticalLayout(text);
        VerticalLayout verticalLayout_2 = new VerticalLayout(grid);
        VerticalLayout verticalLayout_3 = new VerticalLayout(createButtonsLayout());

        horizontalLayout.add(verticalLayout_1,verticalLayout_2,verticalLayout_3);

        add(horizontalLayout);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

//        save.addClickListener(click -> validateAndSave());
//        close.addClickListener(click -> fireEvent(new ProductionOrderForm.CloseEvent(this)));

        return new HorizontalLayout(save,close);
    }

}
