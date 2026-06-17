package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.ProductoMasVendidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long> {

    @Query("""
    SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.ProductoMasVendidoResponseDTO(
        dp.producto.id,
        dp.producto.nombre,
        SUM(dp.cantidad)
    )
    FROM DetallePedidoEntity dp
    WHERE dp.pedido.estadoPedido = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido.ENTREGADO
      AND dp.pedido.estadoPago = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago.PAGADO
    GROUP BY dp.producto.id, dp.producto.nombre
    ORDER BY SUM(dp.cantidad) DESC
""")
    List<ProductoMasVendidoResponseDTO> getTopProductosMasVendidos(Pageable pageable);
}
