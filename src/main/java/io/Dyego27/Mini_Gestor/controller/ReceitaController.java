package io.Dyego27.Mini_Gestor.controller;

import io.Dyego27.Mini_Gestor.dto.ReceitaRequestDTO;
import io.Dyego27.Mini_Gestor.model.Receita;
import io.Dyego27.Mini_Gestor.service.ReceitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }


    @PostMapping
    public ResponseEntity<Receita> criarReceita(@RequestBody ReceitaRequestDTO receitaDto) {

        Receita novaReceita = receitaService.salvarReceita(receitaDto);

        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Receita>> listarReceitas() {
        return ResponseEntity.ok(receitaService.buscarTodas());
    }

    // GET /api/receitas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Receita> buscarReceitaPorId(@PathVariable Long id) {
        return receitaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/receitas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long id) {
        if (!receitaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        receitaService.deletarReceita(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Receita> atualizarReceita(
            @PathVariable Long id,
            @RequestBody ReceitaRequestDTO receitaDto) { // <- AGORA RECEBE O DTO

        if (!receitaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }


        Receita receitaParaSalvar = receitaService.toEntity(receitaDto);


        receitaParaSalvar.setId(id);


        Receita receitaAtualizada = receitaService.salvarEntidade(receitaParaSalvar);

        return ResponseEntity.ok(receitaAtualizada);
    }

    @PutMapping("/{id}/produzir")
    public ResponseEntity<String> produzirReceita(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantidade) {

        try {
            boolean sucesso = receitaService.darBaixaNoEstoque(id, quantidade);

            if (sucesso) {
                return ResponseEntity.ok("Produção de " + quantidade + "x da Receita ID " + id + " realizada. Estoque atualizado.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receita não encontrada.");
            }
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de Estoque: " + e.getMessage());
        }
    }
}