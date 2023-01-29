package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.service.productionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.ResponsiblePersonService;
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
public class NewGoogleSheetsReaderService {
    private final NewProductionOrderService newProductionOrderService;
    private final ResponsiblePersonService responsiblePersonService;
    private final DateNormalizerService dateNormalizerService;
    //final String sheetId_st = "1uCMkk9bvWX84sogpDoG3REINZEo5jx3eVQovgQZF9eo";  //Sant-Tech
    final String sheetId_st = "1jCYuZKFL0vGaPsTUbJ5dedgDLdhHwna722KsMBhyujs"; //Sant-Tech new
    private final OrderStatusNameHandlerService orderStatusNameHandlerService;

    String emptyMessage = "";
    String message = "brak danych";
//    final String range = "Zlecenia !A2:M";
    final String range = "A2:M";

    public NewGoogleSheetsReaderService(NewProductionOrderService newProductionOrderService,
                                        ResponsiblePersonService responsiblePersonService,
                                        DateNormalizerService dateNormalizerService,
                                        OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.newProductionOrderService = newProductionOrderService;
        this.responsiblePersonService = responsiblePersonService;
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
                        NewProductionOrder newProductionOrder = new NewProductionOrder();
                        ResponsiblePerson responsiblePerson = new ResponsiblePerson();

                        //ROW 0
                        String client = (String) row.get(0);

                        //ROW 1
                        String projectNumber = (String) row.get(1);

                        //ROW 2   Attention, now the name consists of 2 words
                        String responsiblePersonName = (String) row.get(2);
                        String responsiblePersonNameTrailed = responsiblePersonName.stripTrailing();// Some resp. person names have spaces at the end...
                        if (Objects.equals(responsiblePersonNameTrailed, "")) {
                            responsiblePersonNameTrailed = message;
                        }
                        String responsiblePersonNameTrailedCpt = responsiblePersonNameTrailed.substring(0, 1).toUpperCase() + responsiblePersonNameTrailed.substring(1);
                        responsiblePerson.setName(responsiblePersonNameTrailedCpt); //Trailed and eventually capitalized

                        //ROW 3
                        String orderDate = emptyMessage;
                        if (row.size() > 3) {
                            //orderDate = (String) row.get(3);
                            orderDate = dateNormalizerService.getNormalizedDate((String) row.get(3));
                        }

                        //ROW 4
                        String orderDeadLine = emptyMessage;
                        if (row.size() > 4) {
                            //orderDeadLine = (String) row.get(4);
                            orderDeadLine = dateNormalizerService.getNormalizedDate((String) row.get(4));
                        }

                        //ROW 5
                        // there is no need to read row 5, cuz it's going to be calculated

                        //ROW 6
                        String planedDispatchDate = emptyMessage;
                        if (row.size() > 6) {
                            //planedDispatchDate = (String) row.get(6);
                            planedDispatchDate = dateNormalizerService.getNormalizedDate((String) row.get(6));
                        }

                        //ROW 7
                        String planedOrderCompletionDate = emptyMessage;
                        if (row.size() > 7) {
                            //planedOrderCompletionDate = (String) row.get(7);
                            planedOrderCompletionDate = dateNormalizerService.getNormalizedDate((String) row.get(7));
                        }

                        //ROW 8
                        String termsOfDelivery = emptyMessage;
                        if (row.size() > 8) {
                            termsOfDelivery = (String) row.get(8);
                        }

                        //ROW 9
                        String orderStatus = emptyMessage;
                        if (row.size() > 9) {
                            orderStatus = (String) row.get(9);
                        }

                        //ROW 10
                        String info = emptyMessage;
                        if (row.size() > 10) {
                            info = (String) row.get(10);
                        }

                        //ROW 11
                        String orderDetailsRef = emptyMessage;
                        if (row.size() > 11) {
                            orderDetailsRef = (String) row.get(11);
                        }

                        //ROW 12
                        String orderBomRef = emptyMessage;
                        if (row.size() > 12) {
                            orderBomRef = (String) row.get(12);
                        }

                        // NewProductionOrder field setting

                        //Client
                        newProductionOrder.setClient(client);
                        //Project number
                        newProductionOrder.setProjectNumber(projectNumber);
                        // Responsible person
                        if (responsiblePersonService.find(responsiblePersonNameTrailedCpt) == null) {
                            responsiblePersonService.save(responsiblePerson);
                            newProductionOrder.setResponsiblePerson(responsiblePerson);
                        } else
                            newProductionOrder.setResponsiblePerson(responsiblePersonService.find(responsiblePersonNameTrailedCpt));
                        //Order date
                        newProductionOrder.setOrderDate(orderDate);
                        //Order deadline
                        newProductionOrder.setOrderDeadLine(orderDeadLine);
                        // planedDispatchDate
                        newProductionOrder.setPlanedDispatchDate(planedDispatchDate);
                        // planedOrderCompletionDate
                        newProductionOrder.setPlanedOrderCompletionDate(planedOrderCompletionDate);
                        // termsOfDelivery
                        newProductionOrder.setTermsOfDelivery(termsOfDelivery);
                        // orderStatus
                        newProductionOrder.setOrderStatus(orderStatusNameHandlerService.setOrderStatus(orderStatus));
                        // info
                        newProductionOrder.setInfo(info);
                        // orderDetailsRef
                        newProductionOrder.setOrderDetailsRef(orderDetailsRef);
                        // orderBomRef
                        newProductionOrder.setOrderBomRef(orderBomRef);


                        //Saving an order
                        try {
                            if (!(client =="")) {
                            newProductionOrderService.save(newProductionOrder);}
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
