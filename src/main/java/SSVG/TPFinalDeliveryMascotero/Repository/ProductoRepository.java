package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Para buscar en BDD productos en general, sin tener division por categoria.
@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    public List<ProductoEntity> findByActivoTrue();
}
