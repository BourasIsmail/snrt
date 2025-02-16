package ma.snrt.snrt.Services;

import ma.snrt.snrt.Entities.Unite;
import ma.snrt.snrt.Repositories.UniteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UniteService {
    @Autowired
    private UniteRepository uniteRepository;

    public List<Unite> getUnites() {
        return uniteRepository.findAll();
    }

    public Unite getUnite(Long id) {
        Optional<Unite> unite = uniteRepository.findById(id);
        if (unite.isPresent()) {
            return unite.get();
        } else {
            throw new RuntimeException("Unite not found");
        }
    }

    public Unite addUnite(Unite unite) {
        return uniteRepository.save(unite);
    }

    public String deleteUnite(Long id) {
        Unite unite = getUnite(id);
        uniteRepository.delete(unite);
        return "Unite deleted";
    }

    public Unite updateUnite(Long id, Unite unite) {
        Unite existingUnite = getUnite(id);
        existingUnite.setNom(unite.getNom());
        existingUnite.setDescription(unite.getDescription());
        return uniteRepository.save(existingUnite);
    }
}
