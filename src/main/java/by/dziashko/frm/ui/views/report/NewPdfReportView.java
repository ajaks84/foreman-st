package by.dziashko.frm.ui.views.report;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.service.newProductionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.utilities.PdfReportService;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.vaadin.alejandro.PdfBrowserViewer;

import java.io.InputStream;

@Route(value = "new-order-report", layout = MainView.class)
@CssImport("./styles/views/order/order-view.css")
public class NewPdfReportView extends VerticalLayout implements HasUrlParameter<Long> {

    NewProductionOrderService newProductionOrderService;
    PdfReportService pdfReportService;
    InputStream is;
    StreamResource resource;
    PdfBrowserViewer viewer;

    public NewPdfReportView(NewProductionOrderService newProductionOrderService,
                            PdfReportService pdfReportService) {
        this.newProductionOrderService = newProductionOrderService;
        this.pdfReportService = pdfReportService;

        setId("order-details-view"); // Fix

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long searchParam) {
        if (searchParam == null && newProductionOrderService == null) {
        } else {
            NewProductionOrder newProductionOrder = newProductionOrderService.find(searchParam);
            is = pdfReportService.generateNewPdfReport(newProductionOrder);
            resource = new StreamResource(newProductionOrder.getProjectNumber()+" raport", () -> is);
            resource.setContentType("application/pdf");
            resource.setCacheTime(0);
            viewer = new PdfBrowserViewer(resource);
            viewer.setHeight("100%");
            add(viewer);
            setHeight("100%");
            UI current = UI.getCurrent();
            current.getPage().setTitle(getTranslation("PDF_Report")+" "+newProductionOrder.getClient());
        }
    }


}
