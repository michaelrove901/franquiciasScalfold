package co.com.bancolombia.model.branch.gateways;

public interface BranchRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
    Flux<Franchise> findAll();
    Mono<Void> deleteById(String id);
    Mono<Franchise> saveAndFlush(Franchise franchise);
}
