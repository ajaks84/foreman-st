package by.dziashko.frm.ui.views.orders;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.repo.ProductionOrderRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductionOrderJPATest {

    ProductionOrder testOrder;

    @Autowired
    ProductionOrderRepo repo;

    @Before
    public void createObject() {
        testOrder = new ProductionOrder();
        testOrder.setClient("testClient");
    }

    @Test
    public void saveTestProductionOrder() {
        repo.save(testOrder);
    }

    @Test
    public void deleteTestProductionOrder() {
        testOrder = repo.getByClient(testOrder.getClient());
        repo.delete(testOrder);
    }

}


