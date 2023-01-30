package by.dziashko.frm.backend.repo.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewProductionOrderRepo extends JpaRepository<NewProductionOrder, Long> {

    @Query("select c from NewProductionOrder c where lower(c.client) like lower(concat('%', :searchTerm, '%'))")
    List<NewProductionOrder> search(@Param("searchTerm") String searchTerm);

//    List<NewProductionOrder> getByOrderReadiness(NewProductionOrder.OrderStatus ready );

    @Query("select c from NewProductionOrder c where  c.orderStatus not like 'Ended' ")
    List<NewProductionOrder> getNotEndedOrders();

//    NewProductionOrder getByOrderNumber(String orderNumber);

    NewProductionOrder getById(Long id);

//    @Modifying
//    @Query( value = "truncate table myTable", nativeQuery = true)  //Fast deleting aternatywa  void deleteAll();
//    void truncateMyTable();
//
//    NewProductionOrder getByClient(String client);
}
