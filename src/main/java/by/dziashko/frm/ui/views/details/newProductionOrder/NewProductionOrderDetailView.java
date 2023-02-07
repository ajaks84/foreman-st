package by.dziashko.frm.ui.views.details.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.service.newProductionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.newProductionOrder.ResponsiblePersonService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

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
    TextField info = new TextField(getTranslation("info"));

    TextField orderDetailsRef = new TextField(getTranslation("Order_Details_Ref"));
    TextField orderBomRef = new TextField(getTranslation("Order_BOM_Ref"));


    Button save = new Button(getTranslation("Save"));
    Button delete = new Button(getTranslation("Delete"));
    Button back = new Button(getTranslation("Back"));
    Button pdfReport = new Button(getTranslation("pdfReport"));

    Button openDetailRef = new Button(getTranslation("Open_Details"));
    Button openBomRef = new Button(getTranslation("Open_BOM"));

    private final String widthVal = "300px";


    public NewProductionOrderDetailView(ResponsiblePersonService responsiblePersonService, NewProductionOrderService newProductionOrderService,
                                        DateNormalizerService dateNormalizerService, OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.responsiblePersonService = responsiblePersonService;
        this.newProductionOrderService = newProductionOrderService;
        this.dateNormalizerService = dateNormalizerService;
        this.orderStatusNameHandlerService=orderStatusNameHandlerService;

        setId("order-details-view");

        List <TextField> arrTextFields = Arrays.asList(client,projectNumber,orderDate,orderDeadLine,orderDelay,planedDispatchDate,
                planedOrderCompletionDate,termsOfDelivery,info,orderDetailsRef,orderBomRef);

        List <ComboBox> arrComboBox = Arrays.asList(responsiblePerson,orderStatus);

        setTextFieldSettings(arrTextFields);
        setComboBoxSettings(arrComboBox);

        binder.bindInstanceFields(this);

        responsiblePerson.setItems(responsiblePersonService.findAll());
        responsiblePerson.setItemLabelGenerator(ResponsiblePerson::getName);

        orderStatus.setItems(NewProductionOrder.OrderStatus.values());
        orderStatus.setItemLabelGenerator(NewProductionOrder.OrderStatus::getStatus);

        HorizontalLayout layoutTop = new HorizontalLayout(client, projectNumber, responsiblePerson, orderDate);
        HorizontalLayout layoutMiddle = new HorizontalLayout( orderDeadLine, orderDelay, planedDispatchDate, planedOrderCompletionDate);
        HorizontalLayout layoutBottom = new HorizontalLayout(termsOfDelivery, info, orderStatus);

        add(layoutTop, layoutMiddle,  layoutBottom, createMixedLayout(), createButtonsLayout()); // layoutBottom_2,layoutMiddle_3,, layoutBottom

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

        return new HorizontalLayout( back, pdfReport);//(save, delete, back, pdfReport);
    }

    private Component createMixedLayout(){

        openDetailRef.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        openDetailRef.addClickListener(event ->  navigateTo(orderDetailsRef.getValue()));

        LOGGER.info(orderDetailsRef.getValue());
        LOGGER.info(orderBomRef.getValue());

//        if (orderDetailsRef.getValue()=="") {
//            openDetailRef.setEnabled(false);
//        }

        openBomRef.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        openBomRef.addClickListener(event -> navigateTo(orderBomRef.getValue()));

//        if (orderBomRef.getValue()=="") {
//            openBomRef.setEnabled(false);
//        }

        return new HorizontalLayout( openDetailRef, openBomRef); // termsOfDelivery, info, orderStatus,
    }

    private void navigateTo(String url) {
        if (url=="") {
            LOGGER.info("Can't navigate to external link: "+url);
            openBomRef.setEnabled(false);
        } else {
            LOGGER.info(url);
            UI.getCurrent().getPage().setLocation(url);
        }
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

    private void setTextFieldSettings(List <TextField> compList){
        for (TextField textField :compList) {
            textField.setReadOnly(true);
            textField.setWidth(widthVal);
        }
    }

    private void setComboBoxSettings(List <ComboBox> compList){
        for (ComboBox comboBox :compList) {
            comboBox.setReadOnly(true);
            comboBox.setWidth(widthVal);
        }
    }

}
