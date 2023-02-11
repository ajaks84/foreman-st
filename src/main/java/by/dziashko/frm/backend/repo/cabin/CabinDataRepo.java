package by.dziashko.frm.backend.repo.cabin;

import by.dziashko.frm.backend.entity.cabin.CabinData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CabinDataRepo extends JpaRepository<CabinData, Long> {
    CabinData getByModelName(String modelName);
}
