package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AntipulgasRepository extends JpaRepository<AntipulgasEntity, Long> {
}
