package jack.labs.mark77.service;

import jack.labs.mark77.entity.Product;
import jack.labs.mark77.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow();
    }
}
