package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.BranchData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BranchRepository extends ReactiveCrudRepository<BranchData, String> {
    Flux<BranchData> findAllByFranchiseId(Long franchiseId);
}
