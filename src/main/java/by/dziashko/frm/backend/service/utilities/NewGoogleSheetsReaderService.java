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
    private final OrderStatusNameHandlerService orderStatusNameHandlerService;

    private final String sheetId_st = "1jCYuZKFL0vGaPsTUbJ5dedgDLdhHwna722KsMBhyujs"; //Sant-Tech new
    private final String range = "A2:M";

    private final String message = "brak danych";
    private List<CellData> cellData = null;

    public NewGoogleSheetsReaderService(NewProductionOrderService newProductionOrderService, ResponsiblePersonService responsiblePersonService, OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.newProductionOrderService = newProductionOrderService;
        this.responsiblePersonService = responsiblePersonService;
        this.orderStatusNameHandlerService = orderStatusNameHandlerService;
    }

    public void getSheetData() throws GeneralSecurityException, IOException {
        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
        List<String> ranges = new ArrayList<>();
        ranges.add(range);
        Spreadsheet spreadsheet = sheetsServiceUtilService.spreadsheets().get(sheetId_st).setRanges(ranges).setIncludeGridData(true).execute(); //.setIncludeGridData(true) is important

        Sheet sheet = spreadsheet.getSheets().get(0);  // 0 represents the first Sheet in your spreadsheet
        GridData gridData = sheet.getData().get(0);

        for (int row = 0; row < gridData.getRowData().size(); row++) {
            cellData = gridData.getRowData().get(row).getValues();

            if (!cellData.isEmpty() && hasCellDataEffectiveValue(0)) {
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
                if (hasCellDataEffectiveValue(2)) {
                    String responsiblePersonName = getCellDataStringValue(2);
                    String responsiblePersonNameTrailed = responsiblePersonName.stripTrailing();// Some resp. person names have spaces at the end...
                    if (Objects.equals(responsiblePersonNameTrailed, "")) {
                        responsiblePersonNameTrailed = message;
                    }
                    String responsiblePersonNameTrailedCpt = responsiblePersonNameTrailed.substring(0, 1).toUpperCase() + responsiblePersonNameTrailed.substring(1);
                    responsiblePerson.setName(responsiblePersonNameTrailedCpt); //Trailed and eventually capitalized

                    if (responsiblePersonService.find(responsiblePersonNameTrailedCpt) == null) {
                        responsiblePersonService.save(responsiblePerson);
                        newProductionOrder.setResponsiblePerson(responsiblePerson);
                    } else
                        newProductionOrder.setResponsiblePerson(responsiblePersonService.find(responsiblePersonNameTrailedCpt));
                }

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
                    newProductionOrderService.save(newProductionOrder);
                } catch (Exception e) {
                    LOGGER.info("Coś poszło nie tak podczas pobierania danych z GoogleSheets... ");
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean hasCellDataEffectiveValue(int cellNumber) {
        return cellData.get(cellNumber).getEffectiveValue() != null;
    }

    private boolean hasCellDataFormattedValue(int cellNumber) {
        return cellData.get(cellNumber).getFormattedValue() != null;
    }

    private boolean hasCellDataNote(int cellNumber) {
        return cellData.get(cellNumber).getNote() != null;
    }

    private boolean hasHyperLink(int cellNumber) {
        return cellData.get(cellNumber).getHyperlink() != null;
    }

    private String getCellDataStringValue(int cellNumber) {
        return cellData.get(cellNumber).getEffectiveValue().getStringValue();
    }

    private String getCellDataFormattedValue(int cellNumber) {
        return cellData.get(cellNumber).getFormattedValue();
    }

    private String getCellDataNote(int cellNumber) {
        return cellData.get(cellNumber).getNote();
    }

    private String getCellHyperLink(int cellNumber) {
        return cellData.get(cellNumber).getHyperlink();
    }

}
