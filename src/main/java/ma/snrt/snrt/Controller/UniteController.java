package ma.snrt.snrt.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.snrt.snrt.Entities.Unite;
import ma.snrt.snrt.Services.UniteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unite")
@RequiredArgsConstructor
@Tag(name = "Unite",
        description = "This API provides the capability to manipulate Unite from a Unite Repository")
public class UniteController {

    private final UniteService uniteService;

    //Endpoint to list all unites
    @GetMapping("/list")
    public ResponseEntity<List<Unite>>  listUnites() {
        List<Unite> unites = uniteService.getUnites();
        return ResponseEntity.ok(unites);
    }

    //Endpoint to get a unite by id
    @GetMapping("/get/{id}")
    public ResponseEntity<Unite> getUnite(Long id) {
        Unite unite = uniteService.getUnite(id);
        return ResponseEntity.ok(unite);
    }

    //Endpoint to add a new unite
    @PostMapping("/add")
    public ResponseEntity<Unite> addUnite(Unite unite) {
        Unite newUnite = uniteService.addUnite(unite);
        return ResponseEntity.ok(newUnite);
    }

    //Endpoint to delete a unite by id
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteUnite(Long id) {
        String message = uniteService.deleteUnite(id);
        return ResponseEntity.ok(message);
    }

    //Endpoint to update a unite by id
    @PutMapping("/update/{id}")
    public ResponseEntity<Unite> updateUnite(Long id, Unite unite) {
        Unite updatedUnite = uniteService.updateUnite(id, unite);
        return ResponseEntity.ok(updatedUnite);
    }


}
