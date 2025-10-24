package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.BranchData;

import java.util.List;

public class BranchMapper {
    public static Branch toModel(BranchData data, List<Product> products) {
        return Branch.builder()
                .id(data.getId())
                .name(data.getName())
                .products(products)
                .build();
    }

    public static BranchData toData(Branch branch, Long franchiseId) {
        BranchData data = new BranchData();
        data.setId(branch.getId());
        data.setName(branch.getName());
        data.setFranchiseId(franchiseId);
        return data;
    }
}
