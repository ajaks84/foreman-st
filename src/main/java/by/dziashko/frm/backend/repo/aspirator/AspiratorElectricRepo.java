package by.dziashko.frm.backend.repo.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorElectric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AspiratorElectricRepo extends JpaRepository<AspiratorElectric, Long> {
    AspiratorElectric getByModelName(String modelName);
}
