package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.BranchData;
import co.com.bancolombia.r2dbc.entity.FranchiseData;
import co.com.bancolombia.r2dbc.entity.ProductData;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import co.com.bancolombia.r2dbc.repository.FranchiseDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FranchisePostgresqlAdapterTest {

    @InjectMocks
    private FranchisePostgresqlAdapter adapter;

    @Mock
    private FranchiseDataRepository franchiseDataRepository;

    @Mock
    private BranchDataRepository branchRepository;

    @Mock
    private ProductDataRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldReturnFranchiseWithId() {

        Franchise franchise = Franchise.builder()
                .name("Franquicia Test")
                .build();

        FranchiseData savedData = new FranchiseData();
        savedData.setId(1L);
        savedData.setName(franchise.getName());

        when(franchiseDataRepository.save(any(FranchiseData.class))).thenReturn(Mono.just(savedData));


        Mono<Franchise> result = adapter.save(franchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getId().equals(1L) && f.getName().equals("Franquicia Test"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnFranchiseWithBranchesAndProducts() {

        Long franchiseId = 1L;
        FranchiseData franchiseData = new FranchiseData();
        franchiseData.setId(franchiseId);
        franchiseData.setName("Franquicia Test");

        BranchData branchData = new BranchData();
        branchData.setId(10L);
        branchData.setName("Sucursal A");
        branchData.setFranchiseId(franchiseId);

        ProductData productData = new ProductData();
        productData.setId(100L);
        productData.setName("Producto X");
        productData.setStock(50);
        productData.setBranchId(branchData.getId());

        when(franchiseDataRepository.findById(franchiseId)).thenReturn(Mono.just(franchiseData));
        when(branchRepository.findAllByFranchiseId(franchiseId)).thenReturn(Flux.just(branchData));
        when(productRepository.findAllByBranchId(branchData.getId())).thenReturn(Flux.just(productData));


        Mono<Franchise> result = adapter.findById(franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(f ->
                        f.getId().equals(franchiseId) &&
                                f.getBranches().size() == 1 &&
                                f.getBranches().get(0).getProducts().size() == 1 &&
                                f.getBranches().get(0).getProducts().get(0).getName().equals("Producto X")
                )
                .verifyComplete();
    }

    @Test
    void update_shouldCallSave() {

        Franchise franchise = Franchise.builder().name("Franquicia Update").build();
        FranchiseData savedData = new FranchiseData();
        savedData.setId(2L);
        savedData.setName(franchise.getName());
        when(franchiseDataRepository.save(any(FranchiseData.class))).thenReturn(Mono.just(savedData));

        Mono<Franchise> result = adapter.update(franchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getId().equals(2L) && f.getName().equals("Franquicia Update"))
                .verifyComplete();
    }
}
