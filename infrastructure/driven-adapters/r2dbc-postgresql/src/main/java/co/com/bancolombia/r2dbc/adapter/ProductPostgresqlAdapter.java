package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.entity.ProductData;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class ProductPostgresqlAdapter implements ProductRepository {

    private final ProductDataRepository productDataRepository;

    @Override
    public Mono<Product> save(Product product, Long branchId) {
        ProductData data = new ProductData();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setStock(product.getStock());
        data.setBranchId(branchId);

        return productDataRepository.save(data)
                .map(ProductMapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productDataRepository.deleteById(id);
    }

    @Override
    public Mono<Product> update(Product product) {
        System.out.println("SQL UPDATE products: SET name = " + product.getName() + " WHERE id = " + product.getId());
        return productDataRepository.updateProductByName(product.getName(), product.getId())
                .map(ProductMapper::toModel);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return productDataRepository.findById(id)
                .map(ProductMapper::toModel);
    }


}
