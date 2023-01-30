package by.dziashko.frm.ui.views.details.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.service.utilities.DateNormalizerService;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Route(value = "order-details", layout = MainView.class)
@CssImport("./styles/views/details/details-view.css")
public class ProductionOrderDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    ProductionOrderService productionOrderService;
    SellerService sellerService;
    DateNormalizerService dateNormalizerService;

    ProductionOrder productionOrder;
    Long searchParam;

    Binder<ProductionOrder> binder = new Binder<>(ProductionOrder.class);

    Dialog dialogSave = new Dialog();
    Dialog dialogDelete = new Dialog();
    Dialog dialogReport = new Dialog();

    ComboBox<Seller> seller = new ComboBox<>(getTranslation("Seller"));
    TextField client = new TextField(getTranslation("Client"));
    TextField projectNumber = new TextField(getTranslation("Project_Number"));
    TextField orderNumber = new TextField(getTranslation("Order_Number"));
    TextField orderDate = new TextField(getTranslation("Order_Date"));
    TextField orderDeadLine = new TextField(getTranslation("Order_Deadline"));
    TextField sendDate = new TextField(getTranslation("Order_Send_Date"));
    TextField orderDelay = new TextField(getTranslation("Order_Delay"));
    ComboBox<ProductionOrder.Readiness> orderReadiness = new ComboBox<>(getTranslation("Order_Readiness"));
    TextField cabinType = new TextField(getTranslation("cabin_type"));
    ComboBox<ProductionOrder.Readiness> cabinReadiness = new ComboBox<>(getTranslation("Readiness"));
    TextField aspiratorType = new TextField(getTranslation("aspirator_type"));
    ComboBox<ProductionOrder.Readiness> aspiratorReadiness = new ComboBox<>(getTranslation("Readiness"));
    TextField separatorType = new TextField(getTranslation("separator_type"));
    ComboBox<ProductionOrder.Readiness> separatorReadiness = new ComboBox<>(getTranslation("Readiness"));
    TextArea additionalOptions = new TextArea(getTranslation("additional_options"));
    ComboBox<ProductionOrder.Readiness> additionalOptionsReadiness = new ComboBox<>(getTranslation("Readiness"));

    Button save = new Button(getTranslation("Save"));
    Button delete = new Button(getTranslation("Delete"));
    Button back = new Button(getTranslation("Back"));
    Button pdfReport = new Button(getTranslation("pdfReport"));


    public ProductionOrderDetailView(SellerService sellerService, ProductionOrderService productionOrderService, DateNormalizerService dateNormalizerService) {
        this.sellerService = sellerService;
        this.productionOrderService = productionOrderService;
        this.dateNormalizerService = dateNormalizerService;
        setId("order-details-view");

        binder.bindInstanceFields(this);

        seller.setItems(sellerService.findAll());
        seller.setItemLabelGenerator(Seller::getName);

        orderReadiness.setItems(ProductionOrder.Readiness.values());
        cabinReadiness.setItems(ProductionOrder.Readiness.values());
        aspiratorReadiness.setItems(ProductionOrder.Readiness.values());
        separatorReadiness.setItems(ProductionOrder.Readiness.values());
        additionalOptionsReadiness.setItems(ProductionOrder.Readiness.values());
        orderDate.setReadOnly(false);
        additionalOptions.getStyle().set("maxHeight", "150px");
        additionalOptions.setWidth("400px");

        HorizontalLayout layoutTop = new HorizontalLayout(seller, client, projectNumber, orderNumber, orderReadiness);
        HorizontalLayout layoutMiddle = new HorizontalLayout(orderDate, orderDeadLine, orderDelay, sendDate);
        HorizontalLayout layoutMiddle_2 = new HorizontalLayout(cabinType, cabinReadiness);
        HorizontalLayout layoutMiddle_3 = new HorizontalLayout(aspiratorType, aspiratorReadiness);
        HorizontalLayout layoutBottom = new HorizontalLayout(separatorType, separatorReadiness);
        HorizontalLayout layoutBottom_2 = new HorizontalLayout(additionalOptions, additionalOptionsReadiness);

        add(layoutTop, layoutMiddle, layoutMiddle_2, layoutMiddle_3, layoutBottom, layoutBottom_2, createButtonsLayout());

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long searchParam) {
        if (searchParam == null && productionOrderService == null) {
        } else {
            this.productionOrder
                    = productionOrderService.find(searchParam);
            this.searchParam = searchParam;
            binder.readBean(this.productionOrder);
            orderDelay.setValue(delayCalcReadiness(productionOrder.getOrderDeadLine(), productionOrder.getOrderReadiness()));
            UI current = UI.getCurrent();
            current.getPage().setTitle(getTranslation("Production_Order_Details") + " " + productionOrder.getClient());
        }
    }

    private String delayCalcReadiness(String date, ProductionOrder.Readiness readiness) {
            return dateNormalizerService.delayCalcFromToday(date,readiness);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        back.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> dialogSave.open());
        delete.addClickListener(click -> dialogDelete.open());
        back.addClickListener(click -> back.getUI().ifPresent(ui -> ui.navigate("orders")));
        pdfReport.addClickListener(event -> dialogReport.open());

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        createSaveDialog();
        createDeleteDialog();
        createReportDialog();
        add(dialogSave, dialogDelete, dialogReport);

        return new HorizontalLayout(save, delete, back, pdfReport);
    }

    private void createSaveDialog() {
        dialogSave.setCloseOnEsc(false);
        dialogSave.setCloseOnOutsideClick(false);

        Button confirmButtonThenSave = new Button(getTranslation("Confirm"), event -> {
            saveOrderName();
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
        dialogSave.add(top, bottom);
    }

    private void createDeleteDialog() {
        dialogDelete.setCloseOnEsc(false);
        dialogDelete.setCloseOnOutsideClick(false);

        Button confirmButtonThenDelete = new Button(getTranslation("Confirm"), event -> {
            deleteOrderName();
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
        dialogDelete.add(top, bottom);
    }

    private void createReportDialog() {
        dialogReport.setCloseOnEsc(false);
        dialogReport.setCloseOnOutsideClick(false);

        Button confirmButtonThenDelete = new Button(getTranslation("Confirm"), event -> {
            generateReport();
            dialogReport.close();
        });
        Button cancelButtonThenDelete = new Button(getTranslation("Cancel"), event -> dialogReport.close());
        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("dialogReport")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenDelete, cancelButtonThenDelete));
        bottom.setJustifyContentMode(JustifyContentMode.CENTER);
        dialogReport.add(top, bottom);
    }

    private void goToPrevView() {
        this.getUI().ifPresent(ui -> ui.navigate("orders"));
    }

    private void saveOrderName() {
        try {
            binder.writeBean(productionOrder);
            productionOrderService.save(productionOrder);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrderName() {
        productionOrderService.delete(productionOrder);
        goToPrevView();
    }

    private void generateReport() {
        if (productionOrder == null) {
            LOGGER.info("Can't navigate to production order report");
        } else {
            Long productionOrderID = productionOrder.getId();
            this.getUI().ifPresent(ui -> ui.navigate("order-report" + "/" + productionOrderID));
        }
    }

}
