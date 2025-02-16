package ma.snrt.snrt.Repositories;

import ma.snrt.snrt.Entities.Unite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniteRepository extends JpaRepository<Unite, Long> {
    Unite findByNom(String nom);
}
