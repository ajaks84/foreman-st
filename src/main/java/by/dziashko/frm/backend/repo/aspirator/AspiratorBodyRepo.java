package by.dziashko.frm.backend.repo.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorBody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AspiratorBodyRepo extends JpaRepository<AspiratorBody, Long> {
    AspiratorBody getByModelName(String modelName);
}
