package by.dziashko.frm.backend.repo.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewProductionOrderRepo extends JpaRepository<NewProductionOrder, Long> {

//    @Query("select c from NewProductionOrder c where lower(c.responsiblePerson) like lower(concat('%', :searchTerm, '%'))")
//    List<NewProductionOrder> search(@Param("searchTerm") String searchTerm);

//    List<NewProductionOrder> getByOrderReadiness(NewProductionOrder.OrderStatus ready );

//    @Query("select c from ProductionOrder c where c.orderReadiness like 'Nie_gotowe' or c.orderReadiness IS NULL ")
//    List<NewProductionOrder> getNotReadyAndEmptyReadiness();

//    NewProductionOrder getByOrderNumber(String orderNumber);

    NewProductionOrder getById(Long id);

//    @Modifying
//    @Query( value = "truncate table myTable", nativeQuery = true)  //Fast deleting aternatywa  void deleteAll();
//    void truncateMyTable();
//
//    NewProductionOrder getByClient(String client);
}
