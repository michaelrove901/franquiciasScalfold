package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.BranchDTO;
import co.com.bancolombia.api.dto.FranchiseDTO;
import co.com.bancolombia.api.dto.ProductDTO;
import co.com.bancolombia.api.dto.StockDTO;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HandlerRest {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .doOnNext(dto -> log.info("Creating franchise with name={}", dto.getName()))
                .doOnNext(dto -> log.info("Creating franchise DTO: {}", dto))
                .flatMap(dto -> franchiseUseCase.createFranchise(dto.getName()))
                .doOnNext(franchise -> log.info("Franchise created: id={}, name={}", franchise.getId(), franchise.getName()))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(e -> log.error("Error creating franchise: {}", e.getMessage()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        return request.bodyToMono(BranchDTO.class)
                .doOnNext(dto -> log.info("Adding branch '{}' to franchise {}", dto.getName(), franchiseId))
                .flatMap(dto -> franchiseUseCase.addBranch(franchiseId, dto.getName()))
                .doOnNext(franchise -> log.info("Branch added successfully to franchise {}", franchiseId))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(e -> log.error("Error adding branch to franchise {}: {}", franchiseId, e.getMessage()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        Long branchId = Long.valueOf(request.pathVariable("branchId"));

        return request.bodyToMono(ProductDTO.class)
                .doOnNext(dto -> log.info("Adding product '{}' to branch '{}' of franchise {}", dto.getName(), branchId, franchiseId))
                .flatMap(dto -> franchiseUseCase.addProductToBranch(franchiseId, branchId, dto.getName(), dto.getStock()))
                .doOnNext(franchise -> log.info("Product added successfully to branch {} of franchise {}", branchId, franchiseId))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(e -> log.error("Error adding product to branch {}: {}", branchId, e.getMessage()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        Long branchId = Long.valueOf(request.pathVariable("branchId"));
        Long productId = Long.valueOf(request.pathVariable("productId"));

        log.info("Removing product '{}' from branch '{}' of franchise {}", productId, branchId, franchiseId);
        return franchiseUseCase.removeProduct(franchiseId, branchId, productId)
                .doOnNext(franchise -> log.info("Product '{}' removed successfully from branch '{}'", productId, branchId))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(e -> log.error("Error removing product '{}' from branch '{}': {}", productId, branchId, e.getMessage()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        Long branchId = Long.valueOf(request.pathVariable("branchId"));
        Long productId = Long.valueOf(request.pathVariable("productId"));

        return request.bodyToMono(StockDTO.class)
                .doOnNext(dto -> log.info("Updating stock of product '{}' in branch '{}' of franchise {} to {}", productId, branchId, franchiseId, dto.getStock()))
                .flatMap(dto -> franchiseUseCase.updateProductStock(franchiseId, branchId, productId, dto.getStock()))
                .doOnNext(franchise -> log.info("Stock updated successfully for product '{}' in branch '{}'", productId, branchId))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(e -> log.error("Error updating stock for product '{}' in branch '{}': {}", productId, branchId, e.getMessage()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> topProductPerBranch(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        log.info("Getting top products per branch for franchise {}", franchiseId);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(franchiseUseCase.topProductPerBranch(franchiseId), FranchiseUseCase.ProductWithBranch.class)
                .doOnTerminate(() -> log.info("Completed fetching top products per branch for franchise {}", franchiseId))
                .onErrorResume(e -> {
                    log.error("Error fetching top products for franchise {}: {}", franchiseId, e.getMessage());
                    return ServerResponse.badRequest().bodyValue(e.getMessage());
                });
    }
}
