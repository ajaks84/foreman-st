package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusNameHandlerService {

    public ProductionOrder.Readiness setOrderStatus(String orderStatus){

        if (orderStatus.contentEquals("Wysłane")) return ProductionOrder.Readiness.Wysłane;
        if (orderStatus.contentEquals("Nie gotowe")) return ProductionOrder.Readiness.Nie_gotowe;
        if (orderStatus.contentEquals("Gotowe")) return ProductionOrder.Readiness.Gotowe;

        return null;
    }

}
