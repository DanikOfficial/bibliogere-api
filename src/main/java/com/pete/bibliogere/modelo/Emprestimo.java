package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Entity(name = "emprestimos")
@Table(name = "emprestimos")
@NoArgsConstructor
public class Emprestimo implements ValidableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    private String utente;

    @Pattern(message = "O email digitado é inválido!", regexp = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@"
            + "((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
    private String email;

    @NotBlank(message = "O contacto é obrigatório!")
//    @Size(min = 9, max = 9, message = "O contacto digitado é inválido. Certifique-se de que o contacto tem {max} caracteres!")
    @Pattern(regexp = "^[0-9]{9}$", message = "O contacto digitado é inválido!")
    @Column(nullable = false)
    private String contacto;

    private Boolean isCurrent;
    @Column(nullable = false, name = "total_obras")
    private int totalObras;

    public Emprestimo(String utente, String contacto, String email) {
        this.utente = utente;
        this.contacto = contacto;
        this.email = email;
    }

    public Emprestimo(String utente, String contacto, String email, List<ItemEmprestimo> itensEmprestimos) {
        this(utente, contacto, email);
    }

    public Emprestimo(Long codigo, String utente, String contacto, String email) {
        this(utente, contacto, email);
        this.codigo = codigo;
    }

}
