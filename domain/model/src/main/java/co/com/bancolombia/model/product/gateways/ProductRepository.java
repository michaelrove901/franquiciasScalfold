package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product,Long branchId);
    Mono<Void> deleteById(Long id);
    Mono<Product> update(Product product);
    Mono<Product> findById(Long id);
}
