package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private static final String ERROR_FRANCHISE_NOT_FOUND = "Franchise not found";
    private static final String ERROR_BRANCH_NOT_FOUND = "Branch not found";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Franchise> createFranchise(String name) {
        Franchise franchise = Franchise.builder()
                .name(name)
                .branches(new ArrayList<>())
                .build();
        return franchiseRepository.save(franchise);
    }

    public Mono<Branch> addBranch(Long franchiseId, String branchName) {
        Branch branch = Branch.builder().name(branchName).build();
        return branchRepository.save(branch, franchiseId);
    }

    public Mono<Franchise> addProductToBranch(Long franchiseId, Long branchId, String productName, int stock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> findBranch(franchise, branchId)
                        .flatMap(branch -> {
                            Product product = Product.builder()
                                    .name(productName)
                                    .stock(stock)
                                    .build();
                            return productRepository.save(product, branchId)
                                    .flatMap(savedProduct -> {
                                        branch.getProducts().add(savedProduct);
                                        return franchiseRepository.update(franchise);
                                    });
                        }));
    }

    public Mono<Franchise> removeProduct(Long franchiseId, Long branchId, Long productId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> findBranch(franchise, branchId)
                        .flatMap(branch ->
                                findProduct(branch, productId)
                                        .flatMap(product ->
                                                productRepository.deleteById(product.getId())
                                                        .then(Mono.fromRunnable(() -> branch.getProducts().remove(product)))
                                                        .thenReturn(franchise)
                                        )
                        )
                );
    }

    public Mono<Franchise> updateProductStock(Long franchiseId, Long branchId, Long productId, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> findBranch(franchise, branchId)
                        .flatMap(branch -> findProduct(branch, productId)
                                .flatMap(product -> {
                                    product.setStock(newStock);
                                    return productRepository.save(product, branchId)
                                            .thenReturn(franchise);
                                })
                        )
                );
    }

    public Flux<ProductWithBranch> topProductPerBranch(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_FRANCHISE_NOT_FOUND)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches())
                        .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                                .sort(Comparator.comparing(Product::getStock).reversed())
                                .next()
                                .map(product -> new ProductWithBranch(branch.getId(), branch.getName(), product))
                        ));
    }

    private Mono<Branch> findBranch(Franchise franchise, Long branchId) {
        return Mono.justOrEmpty(franchise.getBranches().stream()
                        .filter(b -> b.getId().equals(branchId))
                        .findFirst())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_BRANCH_NOT_FOUND)));
    }

    private Mono<Product> findProduct(Branch branch, Long productId) {
        return Mono.justOrEmpty(branch.getProducts().stream()
                        .filter(p -> p.getId() != null && p.getId().equals(productId))
                        .findFirst())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_PRODUCT_NOT_FOUND)));
    }

    public Mono<Franchise> updateName(Long idFranchise, String name) {

        return franchiseRepository.findById(idFranchise)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    franchise.setName(name);
                    return franchiseRepository.update(franchise);
                });
    }

    public Mono<Branch> updateBranch(Long idBranch, String name) {
        return branchRepository.findById(idBranch)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_BRANCH_NOT_FOUND)))
                .flatMap(branch -> {
                    branch.setName(name);
                    return branchRepository.update(branch);
                });
    }

    public Mono<Product> updateProduct(Long idProduct, String name) {
        return productRepository.findById(idProduct)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_PRODUCT_NOT_FOUND)))
                .flatMap(product -> {
                    product.setName(name);
                    return productRepository.update(product);
                });
    }

    public record ProductWithBranch(Long branchId, String branchName, Product product) {
    }
}
