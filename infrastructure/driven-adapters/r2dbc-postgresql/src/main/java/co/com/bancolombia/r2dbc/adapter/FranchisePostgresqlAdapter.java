package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.mapper.BranchMapper;
import co.com.bancolombia.r2dbc.mapper.FranchiseMapper;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import co.com.bancolombia.r2dbc.repository.FranchiseDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchisePostgresqlAdapter implements FranchiseRepository {

    private final FranchiseDataRepository franchiseDataRepository;
    private final BranchDataRepository branchRepository;
    private final ProductDataRepository productRepository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return franchiseDataRepository.save(FranchiseMapper.toData(franchise))
                .map(saved -> {
                    franchise.setId(saved.getId());
                    return franchise;
                });
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return franchiseDataRepository.findById(id)
                .flatMap(franchiseData ->
                        branchRepository.findAllByFranchiseId(franchiseData.getId())
                                .flatMap(branchData ->
                                        productRepository.findAllByBranchId(branchData.getId())
                                                .map(ProductMapper::toModel)  // <-- usar ProductMapper
                                                .collectList()
                                                .map(products -> BranchMapper.toModel(branchData, products))
                                )
                                .collectList()
                                .map(branches -> FranchiseMapper.toModel(franchiseData, branches))
                );
    }




    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return save(franchise);
    }
}
