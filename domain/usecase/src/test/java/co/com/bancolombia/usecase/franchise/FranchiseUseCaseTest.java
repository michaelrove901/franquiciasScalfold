package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FranchiseUseCaseTest {

    FranchiseRepository franchiseRepository;
    BranchRepository branchRepository;
    ProductRepository productRepository;
    FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = mock(FranchiseRepository.class);
        branchRepository = mock(BranchRepository.class);
        productRepository = mock(ProductRepository.class);
        franchiseUseCase = new FranchiseUseCase(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void createFranchise_shouldReturnSavedFranchise() {
        Franchise input = new Franchise(null, "Test Franchise", new ArrayList<>());
        Franchise saved = new Franchise(1L, "Test Franchise", new ArrayList<>());

        when(franchiseRepository.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(franchiseUseCase.createFranchise("Test Franchise"))
                .expectNext(saved)
                .verifyComplete();

        verify(franchiseRepository, times(1)).save(any());
    }

    @Test
    void addBranch_shouldReturnSavedBranch() {
        Branch branch = new Branch(null, "Branch 1", new ArrayList<>());
        Branch savedBranch = new Branch(1L, "Branch 1", new ArrayList<>());

        when(branchRepository.save(any(), eq(1L))).thenReturn(Mono.just(savedBranch));

        StepVerifier.create(franchiseUseCase.addBranch(1L, "Branch 1"))
                .expectNext(savedBranch)
                .verifyComplete();

        verify(branchRepository, times(1)).save(any(), eq(1L));
    }

    @Test
    void addProductToBranch_shouldAddProduct() {
        Product product = new Product(null, "Prod1", 10);
        Product savedProduct = new Product(1L, "Prod1", 10);
        Branch branch = new Branch(1L, "Branch1", new ArrayList<>());
        Franchise franchise = new Franchise(1L, "Franchise1", new ArrayList<>(java.util.List.of(branch)));

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepository.save(any(), eq(branch.getId()))).thenReturn(Mono.just(savedProduct));
        when(franchiseRepository.update(any())).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.addProductToBranch(1L, 1L, "Prod1", 10))
                .expectNext(franchise)
                .verifyComplete();

        verify(productRepository, times(1)).save(any(), eq(branch.getId()));
        verify(franchiseRepository, times(1)).update(any());
    }

    @Test
    void removeProduct_shouldRemoveProductFromBranch() {
        Product product = new Product(1L, "Prod1", 10);
        Branch branch = new Branch(1L, "Branch1", new ArrayList<>(java.util.List.of(product)));
        Franchise franchise = new Franchise(1L, "Franchise1", new ArrayList<>(java.util.List.of(branch)));

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.removeProduct(1L, 1L, 1L))
                .expectNext(franchise)
                .verifyComplete();

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateProductStock_shouldUpdateStock() {
        Product product = new Product(1L, "Prod1", 10);
        Branch branch = new Branch(1L, "Branch1", new ArrayList<>(java.util.List.of(product)));
        Franchise franchise = new Franchise(1L, "Franchise1", new ArrayList<>(java.util.List.of(branch)));

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepository.save(any(), eq(branch.getId()))).thenReturn(Mono.just(product));

        StepVerifier.create(franchiseUseCase.updateProductStock(1L, 1L, 1L, 20))
                .expectNext(franchise)
                .verifyComplete();

        assert product.getStock() == 20;
        verify(productRepository, times(1)).save(any(), eq(branch.getId()));
    }

    @Test
    void topProductPerBranch_shouldReturnTopProduct() {
        Product p1 = new Product(1L, "P1", 5);
        Product p2 = new Product(2L, "P2", 10);
        Branch branch = new Branch(1L, "Branch1", new ArrayList<>(java.util.List.of(p1, p2)));
        Franchise franchise = new Franchise(1L, "F1", new ArrayList<>(java.util.List.of(branch)));

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.topProductPerBranch(1L))
                .expectNextMatches(productWithBranch -> productWithBranch.product().getStock() == 10)
                .verifyComplete();
    }

    @Test
    void addProductToBranch_franchiseNotFound_shouldError() {
        when(franchiseRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.addProductToBranch(99L, 1L, "Prod", 5))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Franchise not found"))
                .verify();
    }

    @Test
    void addProductToBranch_branchNotFound_shouldError() {
        Franchise franchise = new Franchise(1L, "F1", new ArrayList<>());
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.addProductToBranch(1L, 99L, "Prod", 5))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Branch not found"))
                .verify();
    }

}
