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
    @Test
    void update_shouldReturnUpdatedProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Producto Update")
                .stock(20)
                .build();

        ProductData updatedData = new ProductData();
        updatedData.setId(1L);
        updatedData.setName("Producto Update");
        updatedData.setStock(20);

        when(productDataRepository.updateProductByName(product.getName(), product.getId()))
                .thenReturn(Mono.just(updatedData));

        StepVerifier.create(adapter.update(product))
                .expectNextMatches(p -> p.getId().equals(1L) &&
                        p.getName().equals("Producto Update") &&
                        p.getStock() == 20)
                .verifyComplete();
    }

    @Test
    void update_productNotFound_shouldReturnEmpty() {
        Product product = Product.builder()
                .id(99L)
                .name("Nonexistent Product")
                .stock(10)
                .build();

        when(productDataRepository.updateProductByName(product.getName(), product.getId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.update(product))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnProduct() {
        ProductData data = new ProductData();
        data.setId(1L);
        data.setName("Producto X");
        data.setStock(50);
        data.setBranchId(100L);

        when(productDataRepository.findById(1L)).thenReturn(Mono.just(data));

        StepVerifier.create(adapter.findById(1L))
                .expectNextMatches(p -> p.getId().equals(1L) &&
                        p.getName().equals("Producto X") &&
                        p.getStock() == 50)
                .verifyComplete();
    }

    @Test
    void findById_productNotFound_shouldReturnEmpty() {
        when(productDataRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(99L))
                .verifyComplete();
    }
}
