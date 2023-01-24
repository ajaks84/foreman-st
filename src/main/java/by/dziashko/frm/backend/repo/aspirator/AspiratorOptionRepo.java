package by.dziashko.frm.backend.repo.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AspiratorOptionRepo extends JpaRepository<AspiratorOption, Long> {
    AspiratorOption getByModelName(String modelName);
}
