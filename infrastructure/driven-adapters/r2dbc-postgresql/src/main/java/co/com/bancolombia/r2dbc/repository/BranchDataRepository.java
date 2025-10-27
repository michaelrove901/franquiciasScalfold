package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.BranchData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BranchDataRepository extends ReactiveCrudRepository<BranchData, Long> {
    Flux<BranchData> findAllByFranchiseId(Long branchId);
    @Query("UPDATE branches SET name = :name WHERE id = :id RETURNING *")
    Mono<BranchData> updateByName(String name,Long branchId);
}
