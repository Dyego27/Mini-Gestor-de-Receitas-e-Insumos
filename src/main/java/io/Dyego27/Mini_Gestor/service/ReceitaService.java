package io.Dyego27.Mini_Gestor.service;


import io.Dyego27.Mini_Gestor.dto.ReceitaInsumoResponseDTO;
import io.Dyego27.Mini_Gestor.dto.ReceitaRequestDTO;
import io.Dyego27.Mini_Gestor.dto.ReceitaResponseDTO;
import io.Dyego27.Mini_Gestor.exception.EstoqueInsuficienteException;
import io.Dyego27.Mini_Gestor.model.Insumo;
import io.Dyego27.Mini_Gestor.model.Receita;
import io.Dyego27.Mini_Gestor.model.ReceitaInsumo;
import io.Dyego27.Mini_Gestor.repository.InsumoRepository;
import io.Dyego27.Mini_Gestor.repository.ReceitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final InsumoRepository insumoRepository;

    public ReceitaService(ReceitaRepository receitaRepository,InsumoRepository insumoRepository) {
        this.receitaRepository = receitaRepository;
        this.insumoRepository = insumoRepository;
    }


    @Transactional
    public Receita salvarReceita(ReceitaRequestDTO dto) {
        Receita receita = toEntity(dto);
        return receitaRepository.save(receita);
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
                // EXCEÇÃO PERSONALIZADA AGORA É LANÇADA AQUI
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o insumo: " + insumo.getNome() +
                                ". Necessário: " + consumoTotal + ", Disponível: " + insumo.getEstoqueAtual()
                );
            }

            insumo.setEstoqueAtual(insumo.getEstoqueAtual() - consumoTotal);

            insumoRepository.save(insumo);
        }

        return true;
    }


    @Transactional
    public Receita salvarEntidade(Receita receita) {
        return receitaRepository.save(receita);
    }


    public Receita toEntity(ReceitaRequestDTO dto) {
        Receita receita = new Receita();


        receita.setNome(dto.getNome());
        receita.setModoPreparo(dto.getModoPreparo());


        if (dto.getIngredientes() != null) {
            List<ReceitaInsumo> listaInsumos = dto.getIngredientes().stream()
                    .map(ingredienteDto -> {
                        ReceitaInsumo receitaInsumo = new ReceitaInsumo();


                        Long insumoId = ingredienteDto.getInsumoId();
                        Insumo insumo = insumoRepository.findById(insumoId)
                                .orElseThrow(() -> new RuntimeException("Insumo não encontrado com ID: " + insumoId));


                        receitaInsumo.setInsumo(insumo);
                        receitaInsumo.setQuantidadeNecessaria(ingredienteDto.getQuantidadeNecessaria());
                        receitaInsumo.setReceita(receita);

                        return receitaInsumo;
                    })
                    .collect(Collectors.toList());

            receita.setIngredientes(listaInsumos);
        }
        return receita;
    }
    public ReceitaResponseDTO toResponseDTO(Receita receita) {
        ReceitaResponseDTO dto = new ReceitaResponseDTO();

        dto.setId(receita.getId());
        dto.setNome(receita.getNome());
        dto.setModoPreparo(receita.getModoPreparo());


        if (receita.getIngredientes() != null) {
            List<ReceitaInsumoResponseDTO> ingredientesDto = receita.getIngredientes().stream()
                    .map(receitaInsumo -> {
                        ReceitaInsumoResponseDTO insumoDto = new ReceitaInsumoResponseDTO();


                        insumoDto.setInsumoId(receitaInsumo.getInsumo().getId());
                        insumoDto.setQuantidadeNecessaria(receitaInsumo.getQuantidadeNecessaria());


                        insumoDto.setInsumoNome(receitaInsumo.getInsumo().getNome());
                        insumoDto.setUnidadeMedida(receitaInsumo.getInsumo().getUnidadeMedida());

                        return insumoDto;
                    })
                    .collect(Collectors.toList());

            dto.setIngredientes(ingredientesDto);
        }

        return dto;
    }
}

