package by.dziashko.frm.ui.views.service;

import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.backend.service.utilities.GoogleSheetsReaderService;
import by.dziashko.frm.security.SecuredByRole;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SecuredByRole("ROLE_Admin")
@Route(value = "service", layout = MainView.class)
@PageTitle("Service")
@CssImport("./styles/views/about/about-view.css")
public class ServiceView extends HorizontalLayout {

    public ServiceView(GoogleSheetsReaderService googleSheetsReaderService,
                       ProductionOrderService productionOrderService,
                       SellerService sellerService) {
        setId("about-view");

        Button get = new Button("Get actual data");
        get.addClickListener(event -> {
            try {
                googleSheetsReaderService.getSheetsData();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        });

        Button clearPO = new Button("Clear production orders DB");
        clearPO.addClickListener(event -> productionOrderService.deleteAll());

        Button clearSL = new Button("Clear seller DB");
        clearSL.addClickListener(event -> sellerService.deleteAll());

        add (get, clearPO, clearSL);
    }


}
