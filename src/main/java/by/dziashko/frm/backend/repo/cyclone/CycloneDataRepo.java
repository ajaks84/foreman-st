package by.dziashko.frm.backend.repo.cyclone;

import by.dziashko.frm.backend.entity.cyclone.CycloneData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycloneDataRepo extends JpaRepository<CycloneData, Long> {
    CycloneData getByModelName(String modelName);
}
