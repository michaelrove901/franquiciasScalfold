package co.com.bancolombia.model.franchise.gateways;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Flux<Franchise> findAll();
    Mono<Void> deleteById(Long id);
    Mono<Franchise> update(Franchise franchise);
}
