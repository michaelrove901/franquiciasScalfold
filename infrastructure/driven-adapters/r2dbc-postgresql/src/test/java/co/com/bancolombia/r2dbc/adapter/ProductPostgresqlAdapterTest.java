package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductData;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductPostgresqlAdapterTest {

    @InjectMocks
    private ProductPostgresqlAdapter adapter;

    @Mock
    private ProductDataRepository productDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldReturnProductWithId() {

        Product product = Product.builder()
                .name("Producto Test")
                .stock(10)
                .build();

        ProductData savedData = new ProductData();
        savedData.setId(1L);
        savedData.setName(product.getName());
        savedData.setStock(product.getStock());
        savedData.setBranchId(100L);

        when(productDataRepository.save(any(ProductData.class))).thenReturn(Mono.just(savedData));


        Mono<Product> result = adapter.save(product, 100L);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals(1L) &&
                        p.getName().equals("Producto Test") &&
                        p.getStock() == 10)
                .verifyComplete();
    }

    @Test
    void deleteById_shouldCompleteWithoutError() {

        when(productDataRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = adapter.deleteById(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
