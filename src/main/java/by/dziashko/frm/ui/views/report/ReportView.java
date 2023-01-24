package by.dziashko.frm.ui.views.report;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Scope("prototype")
@Route(value = "report", layout = MainView.class)
//@RouteAlias(value = "report", layout = MainView.class)
@CssImport("./styles/views/order/order-view.css")
public class ReportView extends VerticalLayout implements Serializable {
    ProductionOrderService productionOrderService;
    SellerService sellerService;
    Grid<ProductionOrder> grid = new Grid<>(ProductionOrder.class);

    public ReportView(ProductionOrderService productionOrderService, SellerService sellerService) {
        this.sellerService = sellerService;
        this.productionOrderService = productionOrderService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Report"));

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        filterList();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(content);

    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(ProductionOrder::getClient).setHeader(getTranslation("Client"));
        grid.addColumn(ProductionOrder::getCabinType).setHeader(getTranslation("cabin_type")).setSortable(true);;
        grid.addColumn(ProductionOrder::getAspiratorType).setHeader(getTranslation("aspirator_type")).setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    private void navigateTo(ProductionOrder productionOrder) {
        if (productionOrder == null) {
            LOGGER.info("Can't navigate to production order");
        } else {
            Long productionOrderID = productionOrder.getId();
            grid.getUI().ifPresent(ui -> ui.navigate("order-details" + "/" + productionOrderID));
        }
    }

    private void filterList() {
        grid.setItems(productionOrderService.filterReadyAndEmpty(true));
    }
}
