package br.com.crescer.social.repository;

import br.com.crescer.social.entity.Post;
import br.com.crescer.social.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    List<Post> findByUsuarioInOrderByIdDesc(List<Usuario> selecionar); 
}
