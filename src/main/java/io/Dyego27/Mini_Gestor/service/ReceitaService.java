package io.Dyego27.Mini_Gestor.service;


import io.Dyego27.Mini_Gestor.model.Insumo;
import io.Dyego27.Mini_Gestor.model.Receita;
import io.Dyego27.Mini_Gestor.model.ReceitaInsumo;
import io.Dyego27.Mini_Gestor.repository.InsumoRepository;
import io.Dyego27.Mini_Gestor.repository.ReceitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final InsumoRepository insumoRepository;

    public ReceitaService(ReceitaRepository receitaRepository,InsumoRepository insumoRepository) {
        this.receitaRepository = receitaRepository;
        this.insumoRepository = insumoRepository;
    }


    @Transactional
    public Receita salvarReceita(Receita receita) {
        Receita receitaSalva = receitaRepository.save(receita);
        if (receitaSalva.getIngredientes() != null) {
            for (ReceitaInsumo ingrediente : receitaSalva.getIngredientes()) {
                ingrediente.setReceita(receitaSalva);
            }
        }
        return receitaSalva;
    }


    public List<Receita> buscarTodas() {
        return receitaRepository.findAll();
    }

    public Optional<Receita> buscarPorId(Long id) {
        return receitaRepository.findById(id);
    }

    public void deletarReceita(Long id) {
        receitaRepository.deleteById(id);
    }

    @Transactional
    public boolean darBaixaNoEstoque(Long receitaId, int quantidadeProduzida) {
        Optional<Receita> receitaOpt = receitaRepository.findById(receitaId);

        if (receitaOpt.isEmpty()) {
            return false;
        }

        Receita receita = receitaOpt.get();

        for (ReceitaInsumo ingrediente : receita.getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();


            double consumoTotal = ingrediente.getQuantidadeNecessaria() * quantidadeProduzida;


            if (insumo.getEstoqueAtual() < consumoTotal) {

                throw new RuntimeException("Estoque insuficiente para o insumo: " + insumo.getNome());
            }


            insumo.setEstoqueAtual(insumo.getEstoqueAtual() - consumoTotal);


            insumoRepository.save(insumo);
        }

        return true;
    }
}

