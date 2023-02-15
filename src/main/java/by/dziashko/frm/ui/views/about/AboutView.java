package by.dziashko.frm.ui.views.about;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.googleApi.SheetsServiceUtil;
import by.dziashko.frm.ui.views.main.MainView;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./styles/views/about/about-view.css")
public class AboutView extends VerticalLayout {

    private final ProductionOrderService productionOrderService;
    private final SellerService sellerService;

    public AboutView(ProductionOrderService productionOrderService, SellerService sellerService) throws GeneralSecurityException, IOException {
        this.productionOrderService = productionOrderService;
        this.sellerService = sellerService;
        setId("about-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        //final String sheetId = "1uCMkk9bvWX84sogpDoG3REINZEo5jx3eVQovgQZF9eo";  //Sant-Tech
                             //"1fg1XqYONiWy5KL1AY1bhPe3UB2ENG_-hgRjAnZ9lPSc"
        final String sheetId = "1fg1XqYONiWy5KL1AY1bhPe3UB2ENG_-hgRjAnZ9lPSc";   //Private
        //1fg1XqYONiWy5KL1AY1bhPe3UB2ENG_-hgRjAnZ9lPSc
        final String range = "PLAN PRODUKCJI  !A2:AB";
        String message = "brak danych";

        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
        ValueRange response = sheetsServiceUtilService.spreadsheets().values().get(sheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            LOGGER.info("No data found.");
        } else {
            int i = 0;
            for (List row : values) {
                if (row.size() == 0) {
                    i++;
                }
                if (i < 2) {
                    if (!row.isEmpty()) {
                        ProductionOrder productionOrder = new ProductionOrder();
                        Seller slr = new Seller();
                        String sellerName = (String) row.get(0);
                        String sellerNameTrailed = sellerName;
                        sellerName.stripTrailing(); // Some seller names have spaces at the end...
                        slr.setName(sellerName); //Trailed
                        //String projectNumber = (String) row.get(1);
                        String client = (String) row.get(2);
                        String orderNumber = (String) row.get(3);
                        String orderDate = (String) row.get(4);
                        String orderDeadLine = (String) row.get(5);
                        String orderReadiness = message;
                        if (row.size() > 11) {
                            orderReadiness = (String) row.get(11);
                        }
                        String cabinType = message;
                        if (row.size() > 13) {
                            cabinType = (String) row.get(13);
                        }
                        String cabinReadiness = message;
                        if (row.size() > 14) {
                            cabinReadiness = (String) row.get(14);
                        }
                        String aspiratorType = message;
                        if (row.size() > 17) {
                            aspiratorType = (String) row.get(17);
                        }
                        String aspiratorReadiness = message;
                        if (row.size() > 18) {
                            aspiratorReadiness = (String) row.get(18);
                        }
                        String separatorType = message;
                        if (row.size() > 21) {
                            separatorType = (String) row.get(21);
                        }
                        String separatorReadiness = message;
                        if (row.size() > 22) {
                            separatorReadiness = (String) row.get(22);
                        }
                        String additionalOptions = message;
                        if (row.size() > 25) {
                            additionalOptions = (String) row.get(25);
                        }
                        String AdditionalOptionsReadiness = message;
                        if (row.size() > 26) {
                            AdditionalOptionsReadiness = (String) row.get(26);
                        }

                        // Details settings
                        if (sellerService.find(sellerNameTrailed) == null) {
                            sellerService.save(slr);
                            productionOrder.setSeller(slr);
                        } else
                            productionOrder.setSeller(sellerService.find(sellerNameTrailed));
                        //Client
                        productionOrder.setClient(client);
                        //Order number
                        productionOrder.setOrderNumber(orderNumber);
                        //Order date
                        productionOrder.setOrderDate(orderDate);
                        //Order deadline
                        productionOrder.setOrderDeadLine(orderDeadLine);
                        //Order Readiness
                        if (orderReadiness.contentEquals("Wysłane")) {
                            productionOrder.setOrderReadiness(ProductionOrder.Readiness.Sent);
                        }
                        if (orderReadiness.contentEquals("Nie gotowe")) {
                            productionOrder.setOrderReadiness(ProductionOrder.Readiness.NotReady);
                        }
                        if (orderReadiness.contentEquals("Gotowe")) {
                            productionOrder.setOrderReadiness(ProductionOrder.Readiness.Ready);
                        }
                        //Cabin Type
                        productionOrder.setCabinType(cabinType);
                        //Cabin Readiness
                        if (cabinReadiness.contentEquals("Wysłane")) {
                            productionOrder.setCabinReadiness(ProductionOrder.Readiness.Sent);
                        }
                        if (cabinReadiness.contentEquals("Nie gotowe")) {
                            productionOrder.setCabinReadiness(ProductionOrder.Readiness.NotReady);
                        }
                        if (cabinReadiness.contentEquals("Gotowe")) {
                            productionOrder.setCabinReadiness(ProductionOrder.Readiness.Ready);
                        }
                        //Aspirator Type
                        productionOrder.setAspiratorType(aspiratorType);
                        //Aspirator Readiness
                        if (aspiratorReadiness.contentEquals("Wysłane")) {
                            productionOrder.setAspiratorReadiness(ProductionOrder.Readiness.Sent);
                        }
                        if (aspiratorReadiness.contentEquals("Nie gotowe")) {
                            productionOrder.setAspiratorReadiness(ProductionOrder.Readiness.NotReady);
                        }
                        if (aspiratorReadiness.contentEquals("Gotowe")) {
                            productionOrder.setAspiratorReadiness(ProductionOrder.Readiness.Ready);
                        }
                        //Separator Type
                        productionOrder.setSeparatorType(separatorType);
                        //Separator Readiness
                        if (separatorReadiness.contentEquals("Wysłane")) {
                            productionOrder.setSeparatorReadiness(ProductionOrder.Readiness.Sent);
                        }
                        if (separatorReadiness.contentEquals("Nie gotowe")) {
                            productionOrder.setSeparatorReadiness(ProductionOrder.Readiness.NotReady);
                        }
                        if (separatorReadiness.contentEquals("Gotowe")) {
                            productionOrder.setSeparatorReadiness(ProductionOrder.Readiness.Ready);
                        }
                        //Additional Options
                        productionOrder.setAdditionalOptions(additionalOptions);
                        //Additional Options Readiness
                        if (AdditionalOptionsReadiness.contentEquals("Wysłane")) {
                            productionOrder.setAdditionalOptionsReadiness(ProductionOrder.Readiness.Sent);
                        }
                        if (AdditionalOptionsReadiness.contentEquals("Nie gotowe")) {
                            productionOrder.setAdditionalOptionsReadiness(ProductionOrder.Readiness.NotReady);
                        }
                        if (AdditionalOptionsReadiness.contentEquals("Gotowe")) {
                            productionOrder.setAdditionalOptionsReadiness(ProductionOrder.Readiness.Ready);
                        }

                        //Saving an order
                        try {
                            productionOrderService.save(productionOrder);
                        } catch (Exception e) {
                            LOGGER.info("Coś poszło nie tak podczas pobierania danych z GoogleSheets... ");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }


}
