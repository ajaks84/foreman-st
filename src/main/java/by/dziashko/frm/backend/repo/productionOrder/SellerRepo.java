package by.dziashko.frm.backend.repo.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller, Long> {
   Seller getByName (String name);
}
