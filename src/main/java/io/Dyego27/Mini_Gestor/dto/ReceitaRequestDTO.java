package io.Dyego27.Mini_Gestor.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReceitaRequestDTO {
    private String nome;
    private String modoPreparo;


    private List<ReceitaInsumoDTO> ingredientes;
}