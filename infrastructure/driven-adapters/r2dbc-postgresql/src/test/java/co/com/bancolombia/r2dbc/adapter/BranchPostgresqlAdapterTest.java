package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchData;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

class BranchPostgresqlAdapterTest {

    @InjectMocks
    private BranchPostgresqlAdapter adapter;

    @Mock
    private BranchDataRepository branchDataRepository;

    @Mock
    private ProductDataRepository productDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldReturnSavedBranch_withId() {

        Branch branch = Branch.builder()
                .name("Sucursal Test")
                .products(new ArrayList<>())
                .build();
        Long franchiseId = 1L;

        BranchData savedData = new BranchData();
        savedData.setId(100L);
        savedData.setName(branch.getName());
        savedData.setFranchiseId(franchiseId);

        when(branchDataRepository.save(org.mockito.ArgumentMatchers.any(BranchData.class)))
                .thenReturn(Mono.just(savedData));


        Mono<Branch> result = adapter.save(branch, franchiseId);


        StepVerifier.create(result)
                .expectNextMatches(b -> b.getId().equals(100L) && b.getName().equals("Sucursal Test"))
                .verifyComplete();
    }
    @Test
    void findById_shouldReturnBranch() {
        BranchData data = new BranchData();
        data.setId(1L);
        data.setName("Branch 1");
        data.setFranchiseId(10L);

        when(branchDataRepository.findById(1L)).thenReturn(Mono.just(data));

        StepVerifier.create(adapter.findById(1L))
                .expectNextMatches(branch -> branch.getId().equals(1L) &&
                        branch.getName().equals("Branch 1") &&
                        branch.getProducts().isEmpty())
                .verifyComplete();
    }

    @Test
    void findById_notFound_shouldReturnEmpty() {
        when(branchDataRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(99L))
                .verifyComplete();
    }

    @Test
    void update_shouldReturnUpdatedBranch() {
        Branch branch = Branch.builder()
                .id(1L)
                .name("Updated Branch")
                .products(new ArrayList<>())
                .build();

        BranchData updatedData = new BranchData();
        updatedData.setId(1L);
        updatedData.setName(branch.getName());
        updatedData.setFranchiseId(10L);

        when(branchDataRepository.updateByName(branch.getName(), branch.getId()))
                .thenReturn(Mono.just(updatedData));

        StepVerifier.create(adapter.update(branch))
                .expectNextMatches(b -> b.getId().equals(1L) &&
                        b.getName().equals("Updated Branch") &&
                        b.getProducts().isEmpty())
                .verifyComplete();
    }

    @Test
    void update_branchNotFound_shouldReturnEmpty() {
        Branch branch = Branch.builder()
                .id(99L)
                .name("Nonexistent Branch")
                .products(new ArrayList<>())
                .build();

        when(branchDataRepository.updateByName(branch.getName(), branch.getId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.update(branch))
                .verifyComplete();
    }
}
