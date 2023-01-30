package by.dziashko.frm.ui.views.details.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;

@Route(value = "seller-details", layout = MainView.class)
@PageTitle("Seller Details")
// additional details-view.css for this class???
@CssImport("./styles/views/details/details-view.css")
public class SellerDetailView extends VerticalLayout implements HasUrlParameter<String> {

    SellerService sellerService;

    Seller seller;
    String searchParam;

    Binder<Seller> binder = new Binder<>(Seller.class);

    Dialog dialogSave = new Dialog();
    Dialog dialogDelete = new Dialog();
    Dialog dialogConfirm = new Dialog();

    TextField name = new TextField(getTranslation("Name"));
    TextField lastName = new TextField(getTranslation("LastName"));
    IntegerField phoneNumber = new IntegerField(getTranslation("phoneNumber"));
    Button save = new Button(getTranslation("Save"));
    Button delete = new Button(getTranslation("Delete"));
    Button back = new Button(getTranslation("Back"));

    public SellerDetailView(SellerService sellerService) {
        this.sellerService = sellerService;

        setId("seller-details-view");

        binder.bindInstanceFields(this);

        HorizontalLayout layoutTop = new HorizontalLayout(name, lastName, phoneNumber);
        add(layoutTop, createButtonsLayout());

        binder.setBean(seller);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String searchParam) {
        if (searchParam == null && sellerService == null) {}
        else{
            this.seller
                    = sellerService.find(searchParam);
            this.searchParam=searchParam;
            binder.readBean(this.seller);
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        back.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> dialogSave.open());
        delete.addClickListener(click -> dialogDelete.open());
        back.addClickListener(click -> back.getUI().ifPresent(ui -> ui.navigate("sellers")));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        createSaveDialog();
        createDeleteDialog();
        createConfirmDialog();
        add(dialogSave,dialogDelete,dialogConfirm);

        return new HorizontalLayout(save, delete, back);
    }

    private void createSaveDialog(){
        dialogSave.setCloseOnEsc(false);
        dialogSave.setCloseOnOutsideClick(false);

        Button confirmButtonThenSave = new Button(getTranslation("Confirm"), event -> {
            saveNewSeller();
            dialogSave.close();
        });
        Button cancelButtonThenSave = new Button(getTranslation("Cancel"), event -> dialogSave.close());

        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("Confirm_Saving")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenSave, cancelButtonThenSave));
        bottom.setJustifyContentMode(JustifyContentMode.CENTER);
        dialogSave.add(top,bottom);
    }

    private void createDeleteDialog(){
        dialogDelete.setCloseOnEsc(false);
        dialogDelete.setCloseOnOutsideClick(false);

        Button confirmButtonThenDelete = new Button(getTranslation("Confirm"), event -> {
            deleteSeller();
            dialogDelete.close();
        });
        Button cancelButtonThenDelete = new Button(getTranslation("Cancel"), event -> dialogDelete.close());
        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("Confirm_Deleting")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenDelete, cancelButtonThenDelete));
        bottom.setJustifyContentMode(JustifyContentMode.CENTER);
        dialogDelete.add(top,bottom);
    }

    private void createConfirmDialog(){
        dialogConfirm.setCloseOnEsc(false);
        dialogConfirm.setCloseOnOutsideClick(false);

        Button confirmButtonThenSave = new Button(getTranslation("Confirm"), event -> {
            dialogConfirm.close();
        });

        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("Msg_1")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenSave));
        bottom.setJustifyContentMode(JustifyContentMode.CENTER);
        dialogConfirm.add(top,bottom);
    }

    private void goToPrevView() {
        this.getUI().ifPresent(ui -> ui.navigate("sellers"));
    }

    private void saveNewSeller() {
        try {
            binder.writeBean(seller);
            sellerService.save(seller);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void deleteSeller() {
        try {
        sellerService.delete(seller);
        goToPrevView();
        } catch (Exception e) {
            dialogConfirm.open();
        }
    }
}
