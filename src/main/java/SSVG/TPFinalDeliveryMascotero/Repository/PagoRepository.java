package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long>, JpaSpecificationExecutor<PagoEntity> {

    List<PagoEntity> findByPedido_Cliente_Id(Long clienteId);
}
