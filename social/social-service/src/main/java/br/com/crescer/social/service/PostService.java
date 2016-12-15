package br.com.crescer.social.service;

import br.com.crescer.social.entity.Post;
import br.com.crescer.social.entity.Usuario;
import br.com.crescer.social.repository.PostRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    
    @Autowired
    PostRepository repository;
    
    public Page<Post> findAll(Pageable pgbl) {
        return repository.findAll(pgbl);
    }

    public Iterable<Post> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public Post findOne(Long id) {
        return repository.findOne(id);
    }
    
    public Post save(Post post) {
        return repository.save(post);
    }
    
    public List<Post> findAllPosts(Usuario atual) {
        List<Usuario> aFiltrar = atual.getAmigos();
        aFiltrar.add(atual);
        return repository.findByUsuarioInOrderByIdDesc(aFiltrar);
    }
}
