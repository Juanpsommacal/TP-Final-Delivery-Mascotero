package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Service.PedidoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByCalleIgnoreCaseAndNumero(String calle, Integer numero);

    List<PedidoEntity> findByFecha(LocalDate fecha);

    List<PedidoEntity> findByEstadoPedido(EstadoPedido estado);
}
