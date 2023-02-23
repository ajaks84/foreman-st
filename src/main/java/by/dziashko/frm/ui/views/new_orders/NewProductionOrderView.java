package by.dziashko.frm.ui.views.new_orders;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.service.newProductionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.newProductionOrder.ResponsiblePersonService;
import by.dziashko.frm.backend.service.utilities.DateNormalizerService;
import by.dziashko.frm.backend.service.utilities.OrderStatusNameHandlerService;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;


@Scope("prototype")
@Route(value = "new_orders", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@CssImport("./styles/views/order/order-view.css")
@HtmlImport("/style.html")
public class NewProductionOrderView extends VerticalLayout implements Serializable, LocaleChangeObserver {

    private static final long serialVersionUID = 6529685098267757699L;

    OrderStatusNameHandlerService orderStatusNameHandlerService;
    NewProductionOrderService newProductionOrderService;
    ResponsiblePersonService responsiblePersonService;
    Grid<NewProductionOrder> grid = new Grid<>(NewProductionOrder.class);
    TextField filterText = new TextField();
    Checkbox checkboxReady = new Checkbox();
    Checkbox checkboxOrdering = new Checkbox();
    private static final String TD = "<td style=\"border: 1px solid lightgrey; width: 33.3%; padding: 3px;\">";

    private final DateNormalizerService dateNormalizerService;

    public NewProductionOrderView(NewProductionOrderService newProductionOrderService,
                                  ResponsiblePersonService responsiblePersonService,
                                  DateNormalizerService dateNormalizerService,
                                  OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.responsiblePersonService = responsiblePersonService;
        this.newProductionOrderService = newProductionOrderService;
        this.dateNormalizerService = dateNormalizerService;
        this.orderStatusNameHandlerService=orderStatusNameHandlerService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Orders_new"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();
        add(getToolbar(), content);
        searchInList();
        filterListReady(true);
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(NewProductionOrder::getClient).setHeader(getTranslation("Client"));
        grid.addColumn(NewProductionOrder::getProjectNumber).setHeader(getTranslation("Project_Number"));
        grid.addColumn(orderName -> {
            ResponsiblePerson responsiblePerson = orderName.getResponsiblePerson();
            return responsiblePerson == null ? "-" : responsiblePerson.getName();
        }).setHeader(getTranslation("ResponsiblePerson")).setSortable(true);
        grid.addColumn(NewProductionOrder::getOrderDate).setHeader(getTranslation("Order_Date"));
        grid.addColumn(NewProductionOrder::getOrderDeadLine).setHeader(getTranslation("Order_Deadline"));
        grid.addColumn(newProductionOrder -> delayCalcReadiness(newProductionOrder.getOrderDeadLine(), newProductionOrder.getOrderStatus()))
                .setHeader(getTranslation("Order_Delay")).setKey("ordegridrCol");
        grid.addColumn(NewProductionOrder::getPlanedDispatchDate).setHeader(getTranslation("Planed_Dispatch_Date"));
        grid.addColumn(NewProductionOrder::getPlanedOrderCompletionDate).setHeader(getTranslation("Planed_Order_Compl_Date"));
        grid.addColumn(newProductionOrder -> normalizeOrderStatusName(newProductionOrder.getOrderStatus())).setHeader(getTranslation("Order_Status")).setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
        grid.setItems(newProductionOrderService.findAll());
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder(getTranslation("Filter"));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> searchInList());

        checkboxReady.setLabel(getTranslation("NotEnded_Orders"));
        checkboxReady.addValueChangeListener(e -> filterListReady(e.getValue()));

        checkboxOrdering.setLabel(getTranslation("Orders_DetOrdering"));
        checkboxOrdering.addValueChangeListener(e -> filterListWithOrderingPartsStatus(e.getValue()));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, checkboxReady, checkboxOrdering);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void searchInList() {
        grid.setItems(newProductionOrderService.findAll(filterText.getValue()));
    }

    private void filterListReady(Boolean value) {
//        checkboxOrdering.setValue(false);
        checkboxReady.setValue(true);
        grid.setItems(newProductionOrderService.getNotEndedOrders(value));
    }

    private void filterListWithOrderingPartsStatus(Boolean value) {
        checkboxReady.setValue(false);
        checkboxOrdering.setValue(true);
        grid.setItems(newProductionOrderService.getOrdersWithOrderingPartsStatus(value));
    }

    private String delayCalcReadiness(String date, NewProductionOrder.OrderStatus orderStatus) {
        return dateNormalizerService.calcDelayFromToday(date,orderStatus);
    }

    private void navigateTo(NewProductionOrder newProductionOrder) {
        if (newProductionOrder == null) {
            LOGGER.info("Can't navigate to production order");
        } else {
            Long newProductionOrderId = newProductionOrder.getId();
            grid.getUI().ifPresent(ui -> ui.navigate("new-order-details" + "/" + newProductionOrderId));
        }
    }

    private String normalizeOrderStatusName (NewProductionOrder.OrderStatus orderStatus){
        return orderStatusNameHandlerService.normalizeOrderStatusName(orderStatus);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
