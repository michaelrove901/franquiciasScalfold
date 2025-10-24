package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductData;

public class ProductMapper {
    public static ProductData toData(Product model, Long branchId) {
        ProductData data = new ProductData();
        data.setId(model.getId()); // si existe, la DB ignorará null y generará
        data.setName(model.getName());
        data.setStock(model.getStock());
        data.setBranchId(branchId);
        return data;
    }

    // Convierte de entity (DB) a modelo
    public static Product toModel(ProductData data) {
        return Product.builder()
                .id(data.getId())
                .name(data.getName())
                .stock(data.getStock())
                .build();
    }

}
