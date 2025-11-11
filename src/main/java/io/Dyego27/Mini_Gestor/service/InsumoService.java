package io.Dyego27.Mini_Gestor.service;

import io.Dyego27.Mini_Gestor.model.Insumo;
import io.Dyego27.Mini_Gestor.repository.InsumoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;


    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public Insumo salvarInsumo(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    public List<Insumo> buscarTodos() {
        return insumoRepository.findAll();
    }

    public Optional<Insumo> buscarPorId(Long id) {
        return insumoRepository.findById(id);
    }

    public void deletarInsumo(Long id) {
        insumoRepository.deleteById(id);
    }
}
