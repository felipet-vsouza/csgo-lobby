package br.com.crescer.social.repository;

import br.com.crescer.social.entity.Rank;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RankRepository extends PagingAndSortingRepository<Rank, Long> {
    public List<Rank> findAll();
    public Rank findBySequence(int sequence);
    public Rank findTopByOrderBySequenceDesc();
    public Rank findTopByOrderBySequenceAsc();
}
