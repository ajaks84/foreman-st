package by.dziashko.frm.backend.repo.material;

import by.dziashko.frm.backend.entity.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepo extends JpaRepository<Material, Long> {
    Material getByName(String s);
}
