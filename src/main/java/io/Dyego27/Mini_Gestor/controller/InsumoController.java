package io.Dyego27.Mini_Gestor.controller;
import io.Dyego27.Mini_Gestor.model.Insumo;
import io.Dyego27.Mini_Gestor.service.InsumoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }


    @PostMapping
    public ResponseEntity<Insumo> criarInsumo(@RequestBody Insumo insumo) {
        Insumo novoInsumo = insumoService.salvarInsumo(insumo);
        return new ResponseEntity<>(novoInsumo, HttpStatus.CREATED); // 201
    }

    // GET /api/insumos
    @GetMapping
    public ResponseEntity<List<Insumo>> listarInsumos() {
        return ResponseEntity.ok(insumoService.buscarTodos());
    }

    // GET /api/insumos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Insumo> buscarInsumoPorId(@PathVariable Long id) {
        return insumoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/insumos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Insumo> atualizarInsumo(@PathVariable Long id, @RequestBody Insumo insumo) {
        if (!insumoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        insumo.setId(id);
        return ResponseEntity.ok(insumoService.salvarInsumo(insumo));
    }

    // DELETE /api/insumos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInsumo(@PathVariable Long id) {
        if (!insumoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        insumoService.deletarInsumo(id);
        return ResponseEntity.noContent().build();
    }
}
