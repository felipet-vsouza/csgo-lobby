package br.com.crescer.social.service;

import br.com.crescer.social.entity.Usuario;
import br.com.crescer.social.exception.RegraNegocioException;
import br.com.crescer.social.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    public Page<Usuario> findAll(Pageable pgbl) {
        return repository.findAll(pgbl);
    }

    public Iterable<Usuario> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public Usuario findOne(Long id) {
        return repository.findOne(id);
    }

    public Usuario findByEmail(String emUsuario) {
        return repository.findByEmail(emUsuario);
    }

    public Usuario save(Usuario usuario) {
        String senha = new BCryptPasswordEncoder().encode(usuario.getPassword());
        usuario.setPassword(senha);
        return repository.save(usuario);
    }

    public Usuario update(Usuario usuario) {
        return repository.save(usuario);
    }

    public Page<Usuario> findByContentExcept(String token, Long id, Pageable p) {
        token = "%" + token + "%";
        return repository.findByContentExcept(token, token, id, p);
    }

    public void add(Usuario usuario) throws RegraNegocioException {
        Usuario other = this.findByEmail(usuario.getEmail());
        if (other != null) {
            String message = String.format("O e-mail %s já foi cadastrado.", usuario.getEmail());
            throw new RegraNegocioException(message);
        } else {
            this.save(usuario);
        }
    }

    public String addFriend(Usuario atual, Long idUsuarioAAdicionar) throws RegraNegocioException {
        Usuario aAdicionar = this.findOne(idUsuarioAAdicionar);
        if (atual.getId().equals(aAdicionar.getId())) {
            throw new RegraNegocioException("Você não pode enviar um convite de amizade a si próprio.", "is-danger");
        } else if (aAdicionar != null && atual != null) {
            if (aAdicionar.getConvites().contains(atual)) {
                throw new RegraNegocioException(String.format("Você já possui um convite de amizade pendente a %s.", aAdicionar.getUsername()), "is-warning");
            } else if (aAdicionar.getAmigos().contains(atual)) {
                throw new RegraNegocioException(String.format("Você e %s já são amigos.", aAdicionar.getUsername()), "is-warning");
            } else if (atual.getConvites().contains(aAdicionar)) {
                throw new RegraNegocioException(String.format("%s já lhe enviou uma solicitação de amizade.", aAdicionar.getUsername()), "is-warning");
            } else {
                List<Usuario> convites = aAdicionar.getConvites();
                convites.add(atual);
                aAdicionar.setConvites(convites);
                this.update(aAdicionar);
            }
        } else {
            throw new RegraNegocioException("Houve um erro inesperado ao processar o pedido de amizade.", "is-danger");
        }
        return aAdicionar.getUsername();
    }
    
    public String acceptFriend(Usuario atual, Long id) throws RegraNegocioException {
        Usuario aAceitarDB = this.findOne(id);
        Usuario aAceitar = atual
                .getConvites()
                .stream()
                .filter((Usuario u) -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (aAceitarDB == null) {
            throw new RegraNegocioException("Você está tentando adicionar um usuário inexistente.", "is-danger");
        } else if (aAceitar == null) {
            throw new RegraNegocioException(String.format("Você não possui uma solicitação de amizade de %s", aAceitarDB.getUsername()), "is-danger");
        } else {
            this.acceptInvite(atual, aAceitar);
        }
        return aAceitar.getUsername();
    }
    
    public String denyFriend(Usuario atual, Long id) throws RegraNegocioException {
        Usuario aRecustarDB = this.findOne(id);
        Usuario aRecusar = atual
                .getConvites()
                .stream()
                .filter((Usuario u) -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (aRecustarDB == null) {
            throw new RegraNegocioException("Você está tentando recusar o convite de um usuário inexistente.", "is-danger");
        } else if (aRecusar == null) {
            throw new RegraNegocioException(String.format("Você não possui uma solicitação de amizade de %s", aRecustarDB.getUsername()), "is-danger");
        } else {
            this.denyInvite(atual, aRecusar);
        }
        return aRecusar.getUsername();
    }
    
    private void acceptInvite(Usuario a, Usuario b) {
        List<Usuario> convites = a.getConvites();
        convites.remove(b);
        a.setConvites(convites);
        List<Usuario> amigos = a.getAmigos();
        amigos.add(b);
        a.setAmigos(amigos);
        this.update(a);
        amigos = b.getAmigos();
        amigos.add(a);
        b.setAmigos(amigos);
        this.update(b);
    }
    
    private void denyInvite(Usuario a, Usuario b) {
        List<Usuario> convites = a.getConvites();
        convites.remove(b);
        a.setConvites(convites);
        this.update(a);
    }
}
