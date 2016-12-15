package br.com.crescer.social.repository;

import br.com.crescer.social.entity.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

    public Usuario findByEmail(String emUsuario);

    @Query("select u from Usuario u where "
            + "lower(u.username) like lower(:username) and "
            + "lower(u.email) like lower(:email) and "
            + "u.id != :id")
    public Page<Usuario> findByContentExcept(@Param("username") String username, @Param("email") String email, @Param("id") Long id, Pageable p);
}
