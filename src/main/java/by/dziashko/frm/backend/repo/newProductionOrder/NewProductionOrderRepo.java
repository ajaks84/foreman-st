package by.dziashko.frm.backend.repo.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.newProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewProductionOrderRepo extends JpaRepository<newProductionOrder, Long> {

//    @Query("select c from NewProductionOrder c where lower(c.responsiblePerson) like lower(concat('%', :searchTerm, '%'))")
//    List<NewProductionOrder> search(@Param("searchTerm") String searchTerm);

//    List<NewProductionOrder> getByOrderReadiness(NewProductionOrder.OrderStatus ready );

//    @Query("select c from ProductionOrder c where c.orderReadiness like 'Nie_gotowe' or c.orderReadiness IS NULL ")
//    List<NewProductionOrder> getNotReadyAndEmptyReadiness();

//    NewProductionOrder getByOrderNumber(String orderNumber);

    newProductionOrder getById(Long id);

//    @Modifying
//    @Query( value = "truncate table myTable", nativeQuery = true)  //Fast deleting aternatywa  void deleteAll();
//    void truncateMyTable();
//
//    NewProductionOrder getByClient(String client);
}
