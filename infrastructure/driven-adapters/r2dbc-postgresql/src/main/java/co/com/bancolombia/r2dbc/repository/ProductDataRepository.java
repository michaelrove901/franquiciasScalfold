package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ProductData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductDataRepository extends ReactiveCrudRepository<ProductData, Long> {
    Flux<ProductData> findAllByBranchId(Long branchId);
    @Query("UPDATE products SET name = :name WHERE id = :productId RETURNING *")
    Mono<ProductData> updateProductByName(String name, Long id);
}
