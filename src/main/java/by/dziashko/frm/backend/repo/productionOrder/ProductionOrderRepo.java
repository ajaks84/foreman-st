package by.dziashko.frm.backend.repo.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductionOrderRepo extends JpaRepository<ProductionOrder, Long> {

    @Query("select c from ProductionOrder c where lower(c.client) like lower(concat('%', :searchTerm, '%'))")
    List<ProductionOrder> search(@Param("searchTerm") String searchTerm);

    List<ProductionOrder> getByOrderReadiness(ProductionOrder.Readiness ready );

    @Query("select c from ProductionOrder c where c.orderReadiness like 'NotReady' or c.orderReadiness IS NULL ")
    List<ProductionOrder> getNotReadyAndEmptyReadiness();

    ProductionOrder getByOrderNumber(String orderNumber);

    ProductionOrder getById(Long id);

    @Modifying
    @Query( value = "truncate table myTable", nativeQuery = true)  //Fast deleting aternatywa  void deleteAll();
    void truncateMyTable();

    ProductionOrder getByClient(String client);
}
