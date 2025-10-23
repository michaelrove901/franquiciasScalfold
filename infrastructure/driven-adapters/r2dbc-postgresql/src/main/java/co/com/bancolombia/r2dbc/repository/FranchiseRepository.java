package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.FranchiseData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseData, Long> {
}
