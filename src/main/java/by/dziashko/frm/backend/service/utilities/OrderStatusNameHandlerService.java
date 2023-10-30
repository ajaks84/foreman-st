package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusNameHandlerService {

    public ProductionOrder.Readiness setOrderReadiness(String orderStatus){

        if (orderStatus.contentEquals("Wysłane")) return ProductionOrder.Readiness.Sent;
        if (orderStatus.contentEquals("Nie gotowe")) return ProductionOrder.Readiness.NotReady;
        if (orderStatus.contentEquals("Gotowe")) return ProductionOrder.Readiness.Ready;
        return null;
    }

    public String normalizeOrderReadinessName(ProductionOrder.Readiness readiness ){
        if (readiness==ProductionOrder.Readiness.Sent) return "Wysłane";
        if (readiness==ProductionOrder.Readiness.NotReady) return "Nie gotowe";
        if (readiness==ProductionOrder.Readiness.Ready) return "Gotowe";
        return "";
    }

    public NewProductionOrder.OrderStatus setOrderStatus(String orderStatus){

        if (orderStatus.contentEquals("Nowe")) return NewProductionOrder.OrderStatus.New;
        if (orderStatus.contentEquals("Przyjęte do realizacji")) return NewProductionOrder.OrderStatus.AcceptedForProduction;
        if (orderStatus.contentEquals("Projektowanie")) return NewProductionOrder.OrderStatus.Designing;
        if (orderStatus.contentEquals("Zamawianie elementów")) return NewProductionOrder.OrderStatus.OrderingParts;
        if (orderStatus.contentEquals("Gotowość do montażu")) return NewProductionOrder.OrderStatus.ReadyForAssemble;
        if (orderStatus.contentEquals("Montaż")) return NewProductionOrder.OrderStatus.Assembling;
        if (orderStatus.contentEquals("Gotowe do wysyłki")) return NewProductionOrder.OrderStatus.ReadyForDispatch;
        if (orderStatus.contentEquals("Instalacja/Szkolenie")) return NewProductionOrder.OrderStatus.Installation;
        if (orderStatus.contentEquals("Zakończone")) return NewProductionOrder.OrderStatus.Ended;
        if (orderStatus.contentEquals("Wstrzymane!")) return NewProductionOrder.OrderStatus.OnHold;
        if (orderStatus.contentEquals("Analiza")) return NewProductionOrder.OrderStatus.Analyze;

        return null;
    }

    public String normalizeOrderStatusName(NewProductionOrder.OrderStatus orderStatus ){
        if (orderStatus==NewProductionOrder.OrderStatus.New) return "Nowe";
        if (orderStatus==NewProductionOrder.OrderStatus.AcceptedForProduction) return "Przyjęte do realizacji";
        if (orderStatus==NewProductionOrder.OrderStatus.Designing) return "Projektowanie";
        if (orderStatus==NewProductionOrder.OrderStatus.OrderingParts) return "Zamawianie elementów";
        if (orderStatus==NewProductionOrder.OrderStatus.ReadyForAssemble) return "Gotowość do montażu";
        if (orderStatus==NewProductionOrder.OrderStatus.Assembling) return "Montaż";
        if (orderStatus==NewProductionOrder.OrderStatus.ReadyForDispatch) return "Gotowe do wysyłki";
        if (orderStatus==NewProductionOrder.OrderStatus.Installation) return "Instalacja/Szkolenie";
        if (orderStatus==NewProductionOrder.OrderStatus.Ended) return "Zakończone";
        if (orderStatus==NewProductionOrder.OrderStatus.OnHold) return "Wstrzymane!";
        if (orderStatus==NewProductionOrder.OrderStatus.Analyze) return "Analiza";

        return "";
    }
}
