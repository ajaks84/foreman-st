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
public class GoogleSRServiceAdvanced {
    private final NewProductionOrderService newProductionOrderService;
    private final ResponsiblePersonService responsiblePersonService;
    private final DateNormalizerService dateNormalizerService;
    final String sheetId_st = "1jCYuZKFL0vGaPsTUbJ5dedgDLdhHwna722KsMBhyujs"; //Sant-Tech new
    private final OrderStatusNameHandlerService orderStatusNameHandlerService;

    String emptyMessage = "";
    String message = "brak danych";
    //    final String range = "Zlecenia !A2:M";
    final String range = "A2:M";

    public GoogleSRServiceAdvanced(NewProductionOrderService newProductionOrderService,
                                        ResponsiblePersonService responsiblePersonService,
                                        DateNormalizerService dateNormalizerService,
                                        OrderStatusNameHandlerService orderStatusNameHandlerService) {
        this.newProductionOrderService = newProductionOrderService;
        this.responsiblePersonService = responsiblePersonService;
        this.dateNormalizerService = dateNormalizerService;
        this.orderStatusNameHandlerService = orderStatusNameHandlerService;
    }

    public void getCellData() throws GeneralSecurityException, IOException {
        Sheets sheetsServiceUtilService = SheetsServiceUtil.getSheetsService();
//        ValueRange response = sheetsServiceUtilService.spreadsheets().values().get(sheetId_st, range).execute();
        List<String> ranges = new ArrayList<>();
        ranges.add("A2:M");
        Spreadsheet spreadsheet = sheetsServiceUtilService.spreadsheets().get(sheetId_st).setRanges(ranges).execute();

        // 0 represents the first Sheet in your spreadsheet
        int sheetIndex = 0;

        Sheet sheet = spreadsheet.getSheets().get(sheetIndex);
        GridData gridData = sheet.getData().get(0);

        // 1 represents the row 2 in your sheet
        int row = 4;
        RowData rowData = gridData.getRowData().get(row);

        // 1 represents the column M
        int column = 10;
        CellData cellData = rowData.getValues().get(column);

        // This will print the First sheet on my
        // spreadsheet cell B2 strikethrough value
        System.out.println(
                cellData.getNote()
//                        .getEffectiveFormat()
//                        .getTextFormat()
//                        .getStrikethrough()
        );

    }

}
