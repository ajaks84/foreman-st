package by.dziashko.frm.ui.views.new_orders;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.backend.service.utilities.DateNormalizerService;
import by.dziashko.frm.ui.forms.productionOrder.ProductionOrderForm;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;


@Scope("prototype")
@Route(value = "new_orders", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@CssImport("./styles/views/order/order-view.css")
@HtmlImport("/style.html")
public class NewProductionOrderView extends VerticalLayout implements Serializable, LocaleChangeObserver {

    private static final long serialVersionUID = 6529685098267757699L;

    ProductionOrderService productionOrderService;
    SellerService sellerService;
    Grid<ProductionOrder> grid = new Grid<>(ProductionOrder.class);
    TextField filterText = new TextField();
    ProductionOrderForm form;
    Checkbox checkbox = new Checkbox();
    private static String TD = "<td style=\"border: 1px solid lightgrey; width: 33.3%; padding: 3px;\">";

    private final DateNormalizerService dateNormalizerService;

    public NewProductionOrderView(ProductionOrderService productionOrderService, SellerService sellerService, DateNormalizerService dateNormalizerService) {
        this.sellerService = sellerService;
        this.productionOrderService = productionOrderService;
        this.dateNormalizerService = dateNormalizerService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Orders_new"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new ProductionOrderForm(sellerService.findAll());
        form.addListener(ProductionOrderForm.SaveEvent.class, this::saveProductionOrder);
        form.addListener(ProductionOrderForm.DeleteEvent.class, this::deleteProductionOrder);
        form.addListener(ProductionOrderForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        searchInList();
        closeEditor();
        filterList(true);
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(orderName -> {
            Seller seller = orderName.getSeller();
            return seller == null ? "-" : seller.getName();
        }).setHeader(getTranslation("Seller")).setSortable(true);
        grid.addColumn(ProductionOrder::getClient).setHeader(getTranslation("Client"));
        grid.addColumn(ProductionOrder::getProjectNumber).setHeader(getTranslation("Project_Number"));
        grid.addColumn(ProductionOrder::getOrderNumber).setHeader(getTranslation("Order_Number"));
        grid.addColumn(ProductionOrder::getOrderDate).setHeader(getTranslation("Order_Date"));
        grid.addColumn(ProductionOrder::getOrderDeadLine).setHeader(getTranslation("Order_Deadline"));
        grid.addColumn(productionOrder -> delayCalcReadiness(productionOrder.getOrderDeadLine(), productionOrder.getOrderReadiness()))
                .setHeader(getTranslation("Order_Delay")).setKey("ordegridrCol");
        grid.addColumn(ProductionOrder::getOrderReadiness).setHeader(getTranslation("Order_Readiness")).setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
        grid.getColumnByKey("ordegridrCol").setClassNameGenerator(productionOrder -> {
            if (!productionOrder.getOrderDeadLine().equals(" ")) {
                return "warn";
            }
            return null;
        });
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder(getTranslation("Filter"));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> searchInList());

        Button addOrderButton = new Button(getTranslation("New_Order"));
        addOrderButton.addClickListener(click -> addProductionOrder());

        checkbox.setLabel(getTranslation("Only_NOT_Ready_Orders"));
        checkbox.setValue(true);
        checkbox.addValueChangeListener(e -> filterList(e.getValue()));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, checkbox, addOrderButton); //addOrderButton,
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void searchInList() {
        grid.setItems(productionOrderService.findAll(filterText.getValue()));
    }

    void addProductionOrder() {
        grid.asSingleSelect().clear();
        createNewProductionOrder(new ProductionOrder());
    }

    public void createNewProductionOrder(ProductionOrder productionOrder) {
        if (productionOrder == null) {
            closeEditor();
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Seller seller = sellerService.find(user.getUsername());
            if (seller == null) {
                seller = new Seller(user.getUsername());
                sellerService.save(seller);
            }
            productionOrder.setSeller(seller);
            productionOrder.setOrderDate(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            productionOrder.setOrderDeadLine(LocalDate.now().plusWeeks(6).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            productionOrder.setOrderReadiness(ProductionOrder.Readiness.Nie_gotowe);
            form.setProductionOrder(productionOrder);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProductionOrder(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void filterList(Boolean value) {
        grid.setItems(productionOrderService.filterReadyAndEmpty(value));
    }

    private String delayCalcReadiness(String date, ProductionOrder.Readiness readiness) {
        return dateNormalizerService.delayCalcFromToday(date,readiness);
    }

    private void navigateTo(ProductionOrder productionOrder) {
        if (productionOrder == null) {
            LOGGER.info("Can't navigate to production order");
        } else {
            Long productionOrderID = productionOrder.getId();
            grid.getUI().ifPresent(ui -> ui.navigate("order-details" + "/" + productionOrderID));
        }
    }

    private void saveProductionOrder(ProductionOrderForm.SaveEvent event) {
        productionOrderService.save(event.getOrderName());
        searchInList();
        closeEditor();
    }

    private void deleteProductionOrder(ProductionOrderForm.DeleteEvent event) {
        productionOrderService.delete(event.getOrderName());
        searchInList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
