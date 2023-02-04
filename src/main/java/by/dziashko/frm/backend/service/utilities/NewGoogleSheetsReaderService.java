package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.service.newProductionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.newProductionOrder.ResponsiblePersonService;
import by.dziashko.frm.googleApi.SheetsServiceUtil;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Service
public class NewGoogleSheetsReaderService {
    private final NewProductionOrderService newProductionOrderService;
    private final ResponsiblePersonService responsiblePersonService;
    private final DateNormalizerService dateNormalizerService;
    final String sheetId_st = "1jCYuZKFL0vGaPsTUbJ5dedgDLdhHwna722KsMBhyujs"; //Sant-Tech new
    private final OrderStatusNameHandlerService orderStatusNameHandlerService;

    String emptyMessage = "";
    String message = "brak danych";
    List<CellData> cellData = null;
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

    public void getCellData() throws GeneralSecurityException, IOException {
        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
        List<String> ranges = new ArrayList<>();
        ranges.add(range);
        Spreadsheet spreadsheet = sheetsServiceUtilService.spreadsheets()
                                                            .get(sheetId_st)
                                                            .setRanges(ranges)
                                                            .setIncludeGridData(true).execute(); //.setIncludeGridData(true) is important

        // 0 represents the first Sheet in your spreadsheet
        int sheetIndex = 0;

        Sheet sheet = spreadsheet.getSheets().get(sheetIndex);
        GridData gridData = sheet.getData().get(0);

        // 1 represents the row 2 in your sheet
        int row = 8;
        RowData rowData = gridData.getRowData().get(row);

        // 1 represents the column M
        int column = 11;
        CellData cellData = rowData.getValues().get(column);

        // This will print the First sheet on my
        // spreadsheet cell B2 strikethrough value
        System.out.println(
                cellData.getHyperlink()
                //cellData.getNote()
//                        .getEffectiveFormat()
//                        .getTextFormat()
//                        .getStrikethrough()
        );

    }

    public void getSheetData() throws GeneralSecurityException, IOException {
        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
        List<String> ranges = new ArrayList<>();
        ranges.add(range);
        Spreadsheet spreadsheet = sheetsServiceUtilService.spreadsheets()
                .get(sheetId_st)
                .setRanges(ranges)
                .setIncludeGridData(true).execute(); //.setIncludeGridData(true) is important

        // 0 represents the first Sheet in your spreadsheet
        int sheetIndex = 0;

        Sheet sheet = spreadsheet.getSheets().get(sheetIndex);
        GridData gridData = sheet.getData().get(0);
        //List<List<Object>> values=gridData.values();
        System.out.println(gridData.getRowData().size());

//        for (int row = 0; row<gridData.getRowData().size() ; row++)
        for (int row = 0; row<10 ; row++)
        {
            cellData = gridData.getRowData().get(row).getValues();

            if (!cellData.isEmpty()) {
                NewProductionOrder newProductionOrder = new NewProductionOrder();
                ResponsiblePerson responsiblePerson = new ResponsiblePerson();

                //CELL 0
                String client = cellData.get(0).getEffectiveValue().getStringValue();
                System.out.println(client);

                //CELL 1
                String projectNumber = cellData.get(1).getEffectiveValue().getStringValue();
                System.out.println(projectNumber);

                //CELL 2   Attention, now the name consists of 2 words
                String responsiblePersonName = cellData.get(2).getEffectiveValue().getStringValue();
                String responsiblePersonNameTrailed = responsiblePersonName.stripTrailing();// Some resp. person names have spaces at the end...
                if (Objects.equals(responsiblePersonNameTrailed, "")) {
                    responsiblePersonNameTrailed = message;
                }
                String responsiblePersonNameTrailedCpt = responsiblePersonNameTrailed.substring(0, 1).toUpperCase() + responsiblePersonNameTrailed.substring(1);
                System.out.println(responsiblePersonNameTrailedCpt);
                //responsiblePerson.setName(responsiblePersonNameTrailedCpt); //Trailed and eventually capitalized

                // Dates are already formatted, so there is no need in dateNormalizerService

                //CELL 3
                String orderDate = emptyMessage;
                if (hasCellDataFormattedValue(3)) {
                    newProductionOrder.setOrderDate(getCellDataFormattedValue(3));
                    orderDate=getCellDataFormattedValue(3);
                    System.out.println(orderDate);
                }

                //CELL 4
                String orderDeadLine = emptyMessage;
                if (hasCellDataFormattedValue(4)) {
                    orderDeadLine=getCellDataFormattedValue(4);
                    System.out.println(orderDeadLine);
                }

                //CELL 5
                // there is no need to read cellData 5, cuz it's going to be calculated


                //CELL 6
                String planedDispatchDate = emptyMessage;
                if (hasCellDataFormattedValue(6)) {
                    planedDispatchDate=getCellDataFormattedValue(6);
                    System.out.println(planedDispatchDate);
                }

                //CELL 7
                String planedOrderCompletionDate = emptyMessage;
                if (hasCellDataFormattedValue(7)) {
                    planedOrderCompletionDate=getCellDataFormattedValue(7);
                    System.out.println(planedOrderCompletionDate);
                }

                //CELL 8
                String termsOfDelivery = emptyMessage;
                if (hasCellDataEffectiveValue(8)) {
                    termsOfDelivery = getCellDataStringValue(8);
                    System.out.println(termsOfDelivery);
                }

                //CELL 9
                String orderStatus = emptyMessage;
                if (cellData.size() > 9) {
                    orderStatus = cellData.get(9).getEffectiveValue().getStringValue();
                    System.out.println(orderStatus);
                }

                //CELL 10
                String info = emptyMessage;
                if (cellData.size() > 10) {
                    info = cellData.get(10).getNote();
                    System.out.println(info);
                }

                //CELL 11
                String orderDetailsRef = emptyMessage;
                if (cellData.size() > 11) {
                    orderDetailsRef = cellData.get(11).getHyperlink();
                    System.out.println(orderDetailsRef);
                }

                //CELL 12
                String orderBomRef = emptyMessage;
                if (cellData.size() > 12) {
                    orderBomRef = cellData.get(12).getHyperlink();
                    System.out.println(orderBomRef);
                }
            }
        }

    }

    public void getSheetDataAlt() throws GeneralSecurityException, IOException {
        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
        List<String> ranges = new ArrayList<>();
        ranges.add(range);
        Spreadsheet spreadsheet = sheetsServiceUtilService.spreadsheets()
                .get(sheetId_st)
                .setRanges(ranges)
                .setIncludeGridData(true).execute(); //.setIncludeGridData(true) is important

        // 0 represents the first Sheet in your spreadsheet
        int sheetIndex = 0;

        Sheet sheet = spreadsheet.getSheets().get(sheetIndex);
        GridData gridData = sheet.getData().get(0);
        //List<List<Object>> values=gridData.values();
        System.out.println(gridData.getRowData().size());

//        for (int row = 0; row<gridData.getRowData().size() ; row++)
        for (int row = 0; row<10 ; row++)
        {
            cellData = gridData.getRowData().get(row).getValues();

            if (!cellData.isEmpty()) {
                NewProductionOrder newProductionOrder = new NewProductionOrder();
                ResponsiblePerson responsiblePerson = new ResponsiblePerson();

                //CELL 0
                if (hasCellDataEffectiveValue(0)) {
                    newProductionOrder.setClient(getCellDataStringValue(0));
                }

                //CELL 8
                if (hasCellDataEffectiveValue(1)) {
                    newProductionOrder.setProjectNumber(getCellDataStringValue(1));
                }

                //CELL 2   Attention, now the name consists of 2 words
                String responsiblePersonName = getCellDataStringValue(2);
                String responsiblePersonNameTrailed = responsiblePersonName.stripTrailing();// Some resp. person names have spaces at the end...
                if (Objects.equals(responsiblePersonNameTrailed, "")) {
                    responsiblePersonNameTrailed = message;
                }
                String responsiblePersonNameTrailedCpt = responsiblePersonNameTrailed.substring(0, 1).toUpperCase() + responsiblePersonNameTrailed.substring(1);
                //System.out.println(responsiblePersonNameTrailedCpt);
                responsiblePerson.setName(responsiblePersonNameTrailedCpt); //Trailed and eventually capitalized

                if (responsiblePersonService.find(responsiblePersonNameTrailedCpt) == null) {
                    responsiblePersonService.save(responsiblePerson);
                    newProductionOrder.setResponsiblePerson(responsiblePerson);
                } else
                    newProductionOrder.setResponsiblePerson(responsiblePersonService.find(responsiblePersonNameTrailedCpt));

                // Dates are already formatted, so there is no need in dateNormalizerService

                //CELL 3
                if (hasCellDataFormattedValue(3)) {
                    newProductionOrder.setOrderDate(getCellDataFormattedValue(3));
                }

                //CELL 4
                if (hasCellDataFormattedValue(4)) {
                    newProductionOrder.setOrderDeadLine(getCellDataFormattedValue(4));
                }

                //CELL 5
                // there is no need to read cellData 5, cuz it's going to be calculated

                //CELL 6
                if (hasCellDataFormattedValue(6)) {
                    newProductionOrder.setPlanedDispatchDate(getCellDataFormattedValue(6));
                }

                //CELL 7
                if (hasCellDataFormattedValue(7)) {
                    newProductionOrder.setPlanedOrderCompletionDate(getCellDataFormattedValue(7));
                }

                //CELL 8
                if (hasCellDataEffectiveValue(8)) {
                    newProductionOrder.setTermsOfDelivery(getCellDataStringValue(8));
                }

                //CELL 9
                if (hasCellDataEffectiveValue(9)) {
                    newProductionOrder.setOrderStatus(orderStatusNameHandlerService.setOrderStatus(getCellDataStringValue(9)));
                }

                //CELL 10
                if (hasCellDataNote(10)) {
                    newProductionOrder.setInfo(getCellDataNote(10));
                }

                //CELL 11
                if (hasHyperLink(11)) {
                    newProductionOrder.setOrderDetailsRef(getCellHyperLink(11));
                }

                //CELL 12
                if (hasHyperLink(12)) {
                    newProductionOrder.setOrderBomRef(getCellHyperLink(12));
                }

                try {
                    if (!(newProductionOrder.getClient() =="")) {
                        newProductionOrderService.save(newProductionOrder);}
                } catch (Exception e) {
                    LOGGER.info("Coś poszło nie tak podczas pobierania danych z GoogleSheets... ");
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean hasCellDataEffectiveValue (int cellNumber){
        return cellData.get(cellNumber).getEffectiveValue() != null;
    }

    private boolean hasCellDataFormattedValue (int cellNumber){
        return cellData.get(cellNumber).getFormattedValue() != null;
    }

    private boolean hasCellDataNote (int cellNumber){
        return cellData.get(cellNumber).getNote() != null;
    }

    private boolean hasHyperLink (int cellNumber){
        return cellData.get(cellNumber).getHyperlink() != null;
    }

    private String getCellDataStringValue (int cellNumber){
        return cellData.get(cellNumber).getEffectiveValue().getStringValue();
        }

    private String getCellDataFormattedValue (int cellNumber){
        return     cellData.get(cellNumber).getFormattedValue();
    }

    private String getCellDataNote (int cellNumber){
        return cellData.get(cellNumber).getNote();
    }

    private String getCellHyperLink (int cellNumber){
        return cellData.get(cellNumber).getHyperlink();
    }

}
