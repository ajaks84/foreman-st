package by.dziashko.frm.schedule;

import by.dziashko.frm.backend.entity.invoiceItem.InvoiceItem;
import by.dziashko.frm.backend.service.invoiceItem.InvoiceItemService;
import by.dziashko.frm.backend.service.material.MaterialService;
import by.dziashko.frm.backend.service.newProductionOrder.NewProductionOrderService;
import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.utilities.GoogleSheetsReaderService;
import by.dziashko.frm.backend.service.utilities.NewGoogleSheetsReaderService;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class ThreadPoolTaskSchedulerImpl {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private PeriodicTrigger periodicTrigger;

    @Autowired
    private ProductionOrderService productionOrderService;

    @Autowired
    private NewProductionOrderService newProductionOrderService;

    @Autowired
    private GoogleSheetsReaderService googleSheetsReaderService;

    @Autowired
    private NewGoogleSheetsReaderService newGoogleSheetsReaderService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @PostConstruct
    public void scheduleRunnableWithPeriodicTrigger() {
        taskScheduler.schedule(new RunnableTask("Reading data"), periodicTrigger);
    }

    class RunnableTask implements Runnable {

        private final String message;

        public RunnableTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            productionOrderService.deleteAll();
            newProductionOrderService.deleteAll();
            System.out.println("Runnable Task with " + message + " on thread " + Thread.currentThread().getName());
            try {
                googleSheetsReaderService.getSheetsData();
                newGoogleSheetsReaderService.getSheetData();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Lectura de datos OK");

            artNumbersUpdate();
            System.out.println("EAN update OK");

        }

        public void artNumbersUpdate (){
            List <InvoiceItem> invoiceItems = invoiceItemService.findAll();
            for (InvoiceItem invoiceItem : invoiceItems) {
                String ean = invoiceItem.getEan();
                try {
                    invoiceItem.setArtNumber(materialService.getByEan(ean).get(0).getEan());

                }
                catch (Exception error){
                    System.out.println(error.getMessage()+" bad result for: " + ean);
                    invoiceItem.setArtNumber("no Article number found");
                }
            }
            invoiceItemService.deleteAll();
            invoiceItemService.save(invoiceItems);
        }
    }
}