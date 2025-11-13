package io.Dyego27.Mini_Gestor.dto;

import lombok.Data;

@Data
public class ReceitaInsumoResponseDTO {

    private Long insumoId;
    private Double quantidadeNecessaria;


    private String insumoNome;
    private String unidadeMedida;
}
