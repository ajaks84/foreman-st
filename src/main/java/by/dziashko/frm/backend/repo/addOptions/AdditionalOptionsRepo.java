package by.dziashko.frm.backend.repo.addOptions;

import by.dziashko.frm.backend.entity.addOptions.AdditionalOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalOptionsRepo extends JpaRepository<AdditionalOptions, Long> {
    AdditionalOptions getByModelName(String modelName);
}
