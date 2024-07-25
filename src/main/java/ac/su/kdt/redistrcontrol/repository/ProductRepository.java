package ac.su.kdt.redistrcontrol.repository;

import ac.su.kdt.redistrcontrol.domain.Product;
import ac.su.kdt.redistrcontrol.domain.ProductResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void delete(Product product);

    ProductResponseDTO save(ProductResponseDTO product);
}
