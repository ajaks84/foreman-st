package by.dziashko.frm.backend.repo.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AspiratorDataRepo extends JpaRepository<AspiratorData, Long> {
    AspiratorData getByModelName(String modelName);
}
