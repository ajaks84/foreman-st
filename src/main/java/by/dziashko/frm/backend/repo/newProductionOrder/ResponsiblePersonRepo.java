package by.dziashko.frm.backend.repo.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsiblePersonRepo extends JpaRepository<ResponsiblePerson, Long> {
   ResponsiblePerson getByName (String name);
}
