package br.com.crescer.social.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "POST")
public class Post implements Serializable {

    private static final String SQ_NAME = "SEQ_POST";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SQ_NAME)
    @SequenceGenerator(name = SQ_NAME, sequenceName = SQ_NAME, allocationSize = 1)
    @Column(name = "ID_POST")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }
    
    @NotNull(message = "O título da postagem não foi definido.")
    @Size(min = 1, max = 200, message = "O título da postagem deve ter entre {min} e {max} caracteres.")
    @Basic(optional = false)
    @Column(name = "TIT_POST")
    private String titulo;
    
    @NotNull(message = "O corpo da postagem não foi definido.")
    @Size(min = 1, max = 4000, message = "O título da postagem deve ter entre {min} e {max} caracteres.")
    @Basic(optional = false)
    @Column(name = "CRP_POST")
    private String corpo;
}
