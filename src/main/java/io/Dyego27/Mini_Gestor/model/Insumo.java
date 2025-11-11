package io.Dyego27.Mini_Gestor.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "insumos")
@Data
public class Insumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String unidadeMedida;

    private Double estoqueAtual;
}
