package io.Dyego27.Mini_Gestor.model;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "receitas")
@Data
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String modoPreparo;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceitaInsumo> ingredientes;
}