package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.BranchDTO;
import co.com.bancolombia.api.dto.FranchiseDTO;
import co.com.bancolombia.api.dto.ProductDTO;
import co.com.bancolombia.api.dto.StockDTO;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class HandlerRestTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;

    private HandlerRest handlerRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handlerRest = new HandlerRest(franchiseUseCase);
    }

    @Test
    @DisplayName("Test create franchise handler")
    void testCreateFranchiseHandler() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(new FranchiseDTO("Test Franchise")));

        doReturn(Mono.just(franchise)).when(franchiseUseCase).createFranchise("Test Franchise");

        Mono<ServerResponse> result = handlerRest.createFranchise(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test add branch handler")
    void testAddBranchHandler() {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Test Branch");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.bodyToMono(BranchDTO.class)).thenReturn(Mono.just(new BranchDTO("Test Branch")));

        doReturn(Mono.just(branch)).when(franchiseUseCase).addBranch(1L, "Test Branch");

        Mono<ServerResponse> result = handlerRest.addBranch(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test add product handler")
    void testAddProductHandler() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.bodyToMono(ProductDTO.class)).thenReturn(Mono.just(new ProductDTO("Test Product", 10)));

        doReturn(Mono.just(franchise)).when(franchiseUseCase).addProductToBranch(1L, 1L, "Test Product", 10);

        Mono<ServerResponse> result = handlerRest.addProduct(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test remove product handler")
    void testRemoveProductHandler() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.pathVariable("productId")).thenReturn("1");

        doReturn(Mono.just(franchise)).when(franchiseUseCase).removeProduct(1L, 1L, 1L);

        Mono<ServerResponse> result = handlerRest.removeProduct(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test update stock handler")
    void testUpdateStockHandler() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.pathVariable("productId")).thenReturn("1");
        when(request.bodyToMono(StockDTO.class)).thenReturn(Mono.just(new StockDTO(20)));

        doReturn(Mono.just(franchise)).when(franchiseUseCase).updateProductStock(1L, 1L, 1L, 20);

        Mono<ServerResponse> result = handlerRest.updateStock(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test top products per branch handler")
    void testTopProductPerBranchHandler() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");

        Flux<FranchiseUseCase.ProductWithBranch> topProducts = Flux.just(
                new FranchiseUseCase.ProductWithBranch(1L, "Branch 1",
                        new Product(1L, "Product 1", 10))
        );

        doReturn(topProducts).when(franchiseUseCase).topProductPerBranch(1L);

        Mono<ServerResponse> result = handlerRest.topProductPerBranch(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }


    @Test
    @DisplayName("Test update franchise name handler")
    void testUpdateNameFranchiseHandler() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Updated Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(new FranchiseDTO("Updated Franchise")));

        doReturn(Mono.just(franchise)).when(franchiseUseCase).updateName(1L, "Updated Franchise");

        Mono<ServerResponse> result = handlerRest.updateNameFrachise(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test update branch name handler")
    void testUpdateNameBranchHandler() {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Updated Branch");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(new FranchiseDTO("Updated Branch")));

        doReturn(Mono.just(branch)).when(franchiseUseCase).updateBranch(1L, "Updated Branch");

        Mono<ServerResponse> result = handlerRest.updateNameBranch(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test update product name handler")
    void testUpdateNameProductHandler() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("productId")).thenReturn("1");
        when(request.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(new FranchiseDTO("Updated Product")));

        doReturn(Mono.just(product)).when(franchiseUseCase).updateProduct(1L, "Updated Product");

        Mono<ServerResponse> result = handlerRest.updateNameProduct(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> { assert res != null; })
                .verifyComplete();
    }
}
