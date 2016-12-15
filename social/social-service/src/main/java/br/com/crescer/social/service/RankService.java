package br.com.crescer.social.service;

import br.com.crescer.social.entity.Rank;
import br.com.crescer.social.entity.Usuario;
import br.com.crescer.social.exception.RegraNegocioException;
import br.com.crescer.social.repository.RankRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankService {
    
    @Autowired
    RankRepository repositorio;
    
    @Autowired
    UsuarioService usuarioServico;
    
    public List<Rank> findAll() {
        return repositorio.findAll();
    }

    public Rank findOne(Long id) {
        return repositorio.findOne(id);
    }
    
    public Rank findBySequence(int sequence) {
        return repositorio.findBySequence(sequence);
    }
    
    public Rank rankUp(Usuario usuario) throws RegraNegocioException {
        Rank top = repositorio.findTopByOrderBySequenceDesc();
        Rank toReturn;
        if (Objects.equals(usuario.getRank().getSequence(), top.getSequence())) {
            throw new RegraNegocioException("Você não pode mais ser promovido.", "is-danger");
        } else {
            Rank rank = this.findBySequence(usuario.getRank().getSequence() + 1);
            usuario.setRank(rank);
            usuarioServico.update(usuario);
            toReturn = rank;
        }
        return toReturn;
    }
    
    public Rank rankDown(Usuario usuario) throws RegraNegocioException {
        Rank bottom = repositorio.findTopByOrderBySequenceAsc();
        Rank toReturn;
        if (Objects.equals(usuario.getRank().getSequence(), bottom.getSequence())) {
            throw new RegraNegocioException("Você não pode mais ser despromovido.", "is-danger");
        } else {
            Rank rank = this.findBySequence(usuario.getRank().getSequence() - 1);
            usuario.setRank(rank);
            usuarioServico.update(usuario);
            toReturn = rank;
        }
        return toReturn;
    }
}
