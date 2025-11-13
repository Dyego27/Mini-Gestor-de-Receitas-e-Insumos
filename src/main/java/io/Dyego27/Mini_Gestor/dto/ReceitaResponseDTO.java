package io.Dyego27.Mini_Gestor.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReceitaResponseDTO {
    private Long id;
    private String nome;
    private String modoPreparo;


    private List<ReceitaInsumoResponseDTO> ingredientes;
}