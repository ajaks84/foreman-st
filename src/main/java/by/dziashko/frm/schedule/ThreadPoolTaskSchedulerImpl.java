package by.dziashko.frm.schedule;

import by.dziashko.frm.backend.service.productionOrder.ProductionOrderService;
import by.dziashko.frm.backend.service.utilities.GoogleSheetsReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class ThreadPoolTaskSchedulerImpl {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private PeriodicTrigger periodicTrigger;

    @Autowired
    private ProductionOrderService productionOrderService;

    @Autowired
    private GoogleSheetsReaderService googleSheetsReaderService;

    @PostConstruct
    public void scheduleRunnableWithPeriodicTrigger() {
        taskScheduler.schedule(new RunnableTask("Reading data"), periodicTrigger);
    }

    class RunnableTask implements Runnable {

        private String message;

        public RunnableTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            productionOrderService.deleteAll();
            System.out.println("Runnable Task with " + message + " on thread " + Thread.currentThread().getName());
            try {
                googleSheetsReaderService.getSheetsData();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Reading data OK");
        }
    }
}