package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.BranchData;
import co.com.bancolombia.r2dbc.mapper.BranchMapper;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class BranchPostgresqlAdapter implements BranchRepository {
    private final BranchDataRepository branchDataRepository;

    @Override
    public Mono<Branch> save(Branch branch, Long franchiseId) {
        BranchData data = BranchMapper.toData(branch, franchiseId);
        return branchDataRepository.save(data)
                .map(saved -> branch.toBuilder().id(saved.getId()).build());
    }

    @Override
    public Mono<Branch> findById(Long idBranch) {
        return branchDataRepository.findById(idBranch)
                .map(data -> BranchMapper.toModel(data, List.of()));
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        return branchDataRepository.updateByName(branch.getName(),branch.getId() )
                .map(data -> BranchMapper.toModel(data, List.of()));
    }


}