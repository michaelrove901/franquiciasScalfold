package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.franchise.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.BranchData;
import co.com.bancolombia.r2dbc.entity.FranchiseData;
import co.com.bancolombia.r2dbc.entity.ProductData;

import java.util.List;

public class FranchiseMapper {

    public static FranchiseData toData(Franchise model) {
        FranchiseData data = new FranchiseData();
        data.setId(model.getId());
        data.setName(model.getName());
        return data;
    }

    public static BranchData toData(Branch model, Long franchiseId) {
        BranchData data = new BranchData();
        data.setId(model.getId());
        data.setName(model.getName());
        data.setFranchiseId(franchiseId);
        return data;
    }

    public static ProductData toData(Product model) {
        ProductData data = new ProductData();
        data.setId(model.getId());
        data.setProductId(model.getProductId());
        data.setName(model.getName());
        data.setStock(model.getStock());
        data.setBranchId(model.getBranchId());
        return data;
    }

    public static Franchise toModel(FranchiseData data, List<Branch> branches) {
        return Franchise.builder()
                .id(data.getId())
                .name(data.getName())
                .branches(branches)
                .build();
    }

    public static Branch toModel(BranchData data, List<Product> products) {
        return Branch.builder()
                .id(data.getId())
                .name(data.getName())
                .products(products)
                .build();
    }

    public static Product toModel(ProductData data) {
        return Product.builder()
                .id(data.getId())
                .productId(data.getProductId())
                .name(data.getName())
                .stock(data.getStock())
                .branchId(data.getBranchId())
                .build();
    }
}
