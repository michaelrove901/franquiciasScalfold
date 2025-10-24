package co.com.bancolombia.model.branch;

import co.com.bancolombia.model.product.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {
    private Long id;
    private String name;
    @Builder.Default
    private List<Product> products = new ArrayList<>();
}
