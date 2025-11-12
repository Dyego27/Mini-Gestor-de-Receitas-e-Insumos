package io.Dyego27.Mini_Gestor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // NOVO IMPORT!
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "receita_insumos")
@Data
public class ReceitaInsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "receita_id")
    @JsonIgnoreProperties("ingredientes")
    private Receita receita;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    private Double quantidadeNecessaria;
}
