package co.com.bancolombia.model.product.gateways;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Flux<Product> findAllByBranchId(Long branchId);
    Mono<Void> deleteById(Long id);
    Mono<Product> update(Product product);
}
