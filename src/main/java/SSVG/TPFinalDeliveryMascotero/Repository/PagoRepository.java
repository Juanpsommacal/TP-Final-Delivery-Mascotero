package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
}
