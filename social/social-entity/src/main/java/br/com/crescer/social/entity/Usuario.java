package br.com.crescer.social.entity;

import static javax.persistence.CascadeType.ALL;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

    private static final String SQ_NAME = "SEQ_USUARIO";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SQ_NAME)
    @SequenceGenerator(name = SQ_NAME, sequenceName = SQ_NAME, allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @NotNull(message = "O nome de usuário não foi definido.")
    @Size(min = 1, max = 25, message = "o tamanho do nome deve estar entre {min} e {max}.")
    @Basic(optional = false)
    @Column(name = "NM_USUARIO")
    private String username;

    @NotNull(message = "O e-mail não foi definido.")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "O e-mail inserido é inválido.")
    @Size(min = 1, max = 40, message = "o tamanho do e-mail deve estar entre {min} e {max}.")
    @Basic(optional = false)
    @Column(name = "EM_USUARIO")
    private String email;

    @NotNull(message = "A senha não foi definida.")
    @Size(min = 1, max = 200, message = "a senha deve possuir entre {min} e {max} caracteres.")
    @Basic(optional = false)
    @Column(name = "SN_PASSWORD")
    private String password;

    @NotNull(message = "O rank não foi definido.")
    @ManyToOne
    @JoinColumn(name = "ID_RANK")
    private Rank rank;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @NotNull
    @ManyToMany(cascade = ALL)
    private List<Usuario> amigos;

    @NotNull
    @ManyToMany(cascade = ALL)
    private List<Usuario> convites;

    @OneToMany(mappedBy = "usuario")
    List<Post> posts;

    public List<Usuario> getConvites() {
        return convites;
    }

    public void setConvites(List<Usuario> convites) {
        this.convites = convites;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Usuario> amigos) {
        this.amigos = amigos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.username);
        hash = 31 * hash + Objects.hashCode(this.email);
        hash = 31 * hash + Objects.hashCode(this.password);
        hash = 31 * hash + Objects.hashCode(this.rank);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.rank, other.rank)) {
            return false;
        }
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
