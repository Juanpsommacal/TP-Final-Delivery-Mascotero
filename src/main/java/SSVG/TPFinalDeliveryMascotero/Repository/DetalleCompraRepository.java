package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UltimoPrecioCompraResponseDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import SSVG.TPFinalDeliveryMascotero.Model.DetalleCompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompraEntity, Long> {

    // El precio de cada producto en su compra no cancelada mas reciente
    @Query("""
    SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UltimoPrecioCompraResponseDTO(d.producto.id, d.precioUnitario)
    FROM DetalleCompraEntity d
    WHERE d.compra.estadoCompra <> SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoCompra.CANCELADA
      AND d.compra.id = (
          SELECT MAX(otro.compra.id) FROM DetalleCompraEntity otro
          WHERE otro.producto = d.producto
            AND otro.compra.estadoCompra <> SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoCompra.CANCELADA
      )
""")
    List<UltimoPrecioCompraResponseDTO> getUltimosPrecios();

    @Query("SELECT DISTINCT d.producto.id FROM DetalleCompraEntity d WHERE d.compra.proveedor.id = :proveedorId")
    List<Long> getProductoIdsDeProveedor(@Param("proveedorId") Long proveedorId);
}
