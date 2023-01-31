package by.dziashko.frm.ui.views.details.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.service.productionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.ResponsiblePersonService;
import by.dziashko.frm.backend.service.utilities.DateNormalizerService;
import by.dziashko.frm.backend.service.utilities.OrderStatusNameHandlerService;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Route(value = "new-order-details", layout = MainView.class)
@CssImport("./styles/views/details/details-view.css")
public class NewProductionOrderDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    NewProductionOrderService newProductionOrderService;
    ResponsiblePersonService responsiblePersonService;
    DateNormalizerService dateNormalizerService;
    OrderStatusNameHandlerService orderStatusNameHandlerService;

    NewProductionOrder newProductionOrder;
    Long searchParam;

    Binder<NewProductionOrder> binder = new Binder<>(NewProductionOrder.class);

    Dialog dialogSave = new Dialog();
    Dialog dialogDelete = new Dialog();
    Dialog dialogReport = new Dialog();

    TextField client = new TextField(getTranslation("Client"));
    TextField projectNumber = new TextField(getTranslation("Project_Number"));
    ComboBox<ResponsiblePerson> responsiblePerson = new ComboBox<>(getTranslation("ResponsiblePerson")); // do not use ComboBox in a variable name since you use binder
    TextField orderDate = new TextField(getTranslation("Order_Date"));

    TextField orderDeadLine = new TextField(getTranslation("Order_Deadline"));
    TextField orderDelay = new TextField(getTranslation("Order_Delay"));
    TextField planedDispatchDate = new TextField(getTranslation("Planed_Dispatch_Date"));
    TextField planedOrderCompletionDate = new TextField(getTranslation("Planed_Order_Compl_Date"));

    TextField termsOfDelivery = new TextField(getTranslation("Terms_Of_Delivery"));
    ComboBox<NewProductionOrder.OrderStatus> orderStatus = new ComboBox<>(getTranslation("Order_Status"));

    Button save = new Button(getTranslation("Save"));
    Button delete = new Button(getTranslation("Delete"));
    Button back = new Button(getTranslation("Back"));
    Button pdfReport = new Button(getTranslation("pdfReport"));


    public NewProductionOrderDetailView(ResponsiblePersonService responsiblePersonService, NewProductionOrderService newProductionOrderService,
                                        DateNormalizerService dateNormalizerService, OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.responsiblePersonService = responsiblePersonService;
        this.newProductionOrderService = newProductionOrderService;
        this.dateNormalizerService = dateNormalizerService;
        this.orderStatusNameHandlerService=orderStatusNameHandlerService;

        setId("order-details-view");

        binder.bindInstanceFields(this);

        responsiblePerson.setItems(responsiblePersonService.findAll());
        responsiblePerson.setItemLabelGenerator(ResponsiblePerson::getName);


//        orderStatus.setItems(NewProductionOrder.OrderStatus.values());
        orderStatus.setItems(NewProductionOrder.OrderStatus.values());

        System.out.println(orderStatus.getPlaceholder());
//        orderStatus.getPlaceholder()

        orderDate.setReadOnly(false);
//        additionalOptions.getStyle().set("maxHeight", "150px");
//        additionalOptions.setWidth("400px");

        HorizontalLayout layoutTop = new HorizontalLayout(client, projectNumber, responsiblePerson, orderDate);
        HorizontalLayout layoutMiddle = new HorizontalLayout( orderDeadLine, orderDelay, planedDispatchDate, planedOrderCompletionDate);
        HorizontalLayout layoutMiddle_2 = new HorizontalLayout(termsOfDelivery, orderStatus);
//        HorizontalLayout layoutMiddle_3 = new HorizontalLayout(aspiratorType, aspiratorReadiness);
//        HorizontalLayout layoutBottom = new HorizontalLayout(separatorType, separatorReadiness);
//        HorizontalLayout layoutBottom_2 = new HorizontalLayout(additionalOptions, additionalOptionsReadiness);

        add(layoutTop, layoutMiddle, layoutMiddle_2,  createButtonsLayout()); //layoutMiddle_3, layoutBottom, layoutBottom_2,

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long searchParam) {
        if (searchParam == null && newProductionOrderService == null) {
        } else {
            this.newProductionOrder
                    = newProductionOrderService.find(searchParam);
            this.searchParam = searchParam;
            binder.readBean(this.newProductionOrder);
            orderDelay.setValue(delayCalcReadiness(newProductionOrder.getOrderDeadLine(), newProductionOrder.getOrderStatus()));
            UI current = UI.getCurrent();
            current.getPage().setTitle(getTranslation("Production_Order_Details") + " " + newProductionOrder.getClient());
        }
    }

    private String delayCalcReadiness(String date, NewProductionOrder.OrderStatus orderStatus) {
        return dateNormalizerService.calcDelayFromToday(date,orderStatus);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        back.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> dialogSave.open());
        delete.addClickListener(click -> dialogDelete.open());
        back.addClickListener(click -> back.getUI().ifPresent(ui -> ui.navigate("new_orders")));
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
        top.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("Confirm_Saving")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenSave, cancelButtonThenSave));
        bottom.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
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
        top.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("Confirm_Deleting")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenDelete, cancelButtonThenDelete));
        bottom.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
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
        top.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text(getTranslation("dialogReport")));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenDelete, cancelButtonThenDelete));
        bottom.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        dialogReport.add(top, bottom);
    }

    private void goToPrevView() {
        this.getUI().ifPresent(ui -> ui.navigate("new_orders"));
    }

    private void saveOrderName() {
        try {
            binder.writeBean(newProductionOrder);
            newProductionOrderService.save(newProductionOrder);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrderName() {
        newProductionOrderService.delete(newProductionOrder);
        goToPrevView();
    }

    private void generateReport() {
        if (newProductionOrder == null) {
            LOGGER.info("Can't navigate to production order report");
        } else {
            Long productionOrderID = newProductionOrder.getId();
            this.getUI().ifPresent(ui -> ui.navigate("order-report" + "/" + productionOrderID));
        }
    }

    private String normalizeOrderStatusName (NewProductionOrder.OrderStatus orderStatus){
        return orderStatusNameHandlerService.normalizeOrderStatusName(orderStatus);
    }

}
