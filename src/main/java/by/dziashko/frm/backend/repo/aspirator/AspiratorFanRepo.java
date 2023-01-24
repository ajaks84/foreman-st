package by.dziashko.frm.backend.repo.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorFan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AspiratorFanRepo extends JpaRepository<AspiratorFan, Long> {
    AspiratorFan getByModelName(String modelName);
}
