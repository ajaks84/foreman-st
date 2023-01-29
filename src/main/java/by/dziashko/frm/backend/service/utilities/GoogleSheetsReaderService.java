package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.googleApi.SheetsServiceUtil;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Service
public class GoogleSheetsReaderService {
    private final ProductionOrderService productionOrderService;
    private final SellerService sellerService;
    private final DateNormalizerService dateNormalizerService;
    private final OrderStatusNameHandlerService orderStatusNameHandlerService;
    final String sheetId_st = "1uCMkk9bvWX84sogpDoG3REINZEo5jx3eVQovgQZF9eo";  //Sant-Tech
    String emptyMessage = "";
    String message = "brak danych";
    final String range = "PLAN PRODUKCJI  !A2:AB";

    public GoogleSheetsReaderService(ProductionOrderService productionOrderService, SellerService sellerService,
                                     DateNormalizerService dateNormalizerService, OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.productionOrderService = productionOrderService;
        this.sellerService = sellerService;
        this.dateNormalizerService = dateNormalizerService;
        this.orderStatusNameHandlerService = orderStatusNameHandlerService;
    }

    public void getSheetsData() throws GeneralSecurityException, IOException {
        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
        ValueRange response = sheetsServiceUtilService.spreadsheets().values().get(sheetId_st, range).execute();
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
                        String sellerNameTrailed = sellerName.stripTrailing();// Some seller names have spaces at the end...
                        if (Objects.equals(sellerNameTrailed, "")) {
                            sellerNameTrailed = message;
                        }
                        String sellerNameTrailedCpt = sellerNameTrailed.substring(0, 1).toUpperCase() + sellerNameTrailed.substring(1);

                        slr.setName(sellerNameTrailedCpt); //Trailed and eventually capitalized
                        String projectNumber = (String) row.get(2);
                        String client = (String) row.get(3);
                        String orderNumber = emptyMessage;
                        if (row.size() > 4) {
                            orderNumber = (String) row.get(4);
                        }
                        String orderDate = emptyMessage;
                        if (row.size() > 5) {
                            //orderDate = (String) row.get(5);
                            orderDate = dateNormalizerService.getNormalizedDate((String) row.get(5));
                        }
                        String orderDeadLine = emptyMessage;
                        if (row.size() > 6) {
                            //orderDeadLine = (String) row.get(6);
                            orderDeadLine = dateNormalizerService.getNormalizedDate((String) row.get(6));
                        }
                        String orderReadiness = emptyMessage;
                        if (row.size() > 12) {
                            orderReadiness = (String) row.get(12);
                        }
                        String cabinType = emptyMessage;
                        if (row.size() > 14) {
                            cabinType = (String) row.get(14);
                        }
                        String cabinReadiness = emptyMessage;
                        if (row.size() > 15) {
                            cabinReadiness = (String) row.get(15);
                        }
                        String aspiratorType = emptyMessage;
                        if (row.size() > 18) {
                            aspiratorType = (String) row.get(18);
                        }
                        String aspiratorReadiness = emptyMessage;
                        if (row.size() > 19) {
                            aspiratorReadiness = (String) row.get(19);
                        }
                        String separatorType = emptyMessage;
                        if (row.size() > 22) {
                            separatorType = (String) row.get(22);
                        }
                        String separatorReadiness = emptyMessage;
                        if (row.size() > 23) {
                            separatorReadiness = (String) row.get(23);
                        }
                        String additionalOptions = emptyMessage;
                        if (row.size() > 25) {
                            additionalOptions = (String) row.get(25);
                        }
                        String additionalOptionsReadiness = emptyMessage;
                        if (row.size() > 26) {
                            additionalOptionsReadiness = (String) row.get(26);
                        }
                        // Details settings
                        if (sellerService.find(sellerNameTrailedCpt) == null) {
                            sellerService.save(slr);
                            productionOrder.setSeller(slr);
                        } else
                            productionOrder.setSeller(sellerService.find(sellerNameTrailedCpt));

                        // Completing a productionOrder

                        //Client
                        productionOrder.setClient(client);
                        //Project number
                        productionOrder.setProjectNumber(projectNumber);
                        //Order number
                        productionOrder.setOrderNumber(orderNumber);
                        //Order date
                        productionOrder.setOrderDate(orderDate);
                        //Order deadline
                        productionOrder.setOrderDeadLine(orderDeadLine);
                        //Order Readiness
                        productionOrder.setOrderReadiness(orderStatusNameHandlerService.setOrderReadiness(orderReadiness));
                        //Cabin Type
                        productionOrder.setCabinType(cabinType);
                        //Cabin Readiness
                        productionOrder.setCabinReadiness(orderStatusNameHandlerService.setOrderReadiness(cabinReadiness));
                        //Aspirator Type
                        productionOrder.setAspiratorType(aspiratorType);
                        //Aspirator Readiness
                        productionOrder.setAspiratorReadiness(orderStatusNameHandlerService.setOrderReadiness(aspiratorReadiness));
                        //Separator Type
                        productionOrder.setSeparatorType(separatorType);
                        //Separator Readiness
                        productionOrder.setSeparatorReadiness(orderStatusNameHandlerService.setOrderReadiness(separatorReadiness));
                        //Additional Options
                        productionOrder.setAdditionalOptions(additionalOptions);
                        //Additional Options Readiness
                        productionOrder.setAdditionalOptionsReadiness(orderStatusNameHandlerService.setOrderReadiness(additionalOptionsReadiness));

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
