package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.CantidadPedidosPorEstadoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.TicketPromedioResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.VentasPorMesResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.VentasPorRangoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Service.PedidoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByCalleIgnoreCaseAndNumero(String calle, Integer numero);

    List<PedidoEntity> findByFecha(LocalDate fecha);

    List<PedidoEntity> findByEstadoPedido(EstadoPedido estado);

    List<PedidoEntity> findByEstadoPago(EstadoPago estado);

    @Query("""
        SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.VentasPorMesResponseDTO(
            YEAR(p.fecha), 
            MONTH(p.fecha), 
            COUNT(p), 
            SUM(p.montoTotal)
        )
        FROM PedidoEntity p
        WHERE YEAR(p.fecha) = :anio 
          AND MONTH(p.fecha) = :mes
          AND p.estadoPedido = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido.ENTREGADO
          AND p.estadoPago = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago.PAGADO
        GROUP BY YEAR(p.fecha), MONTH(p.fecha)
    """)
    Optional<VentasPorMesResponseDTO> getVentasPorMes(
            @Param("anio") Integer anio,
            @Param("mes") Integer mes
    );

    @Query("""
    SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.VentasPorRangoResponseDTO(
        :fechaInicio,
        :fechaFin,
        COUNT(p),
        SUM(p.montoTotal)
    )
    FROM PedidoEntity p
    WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin
      AND p.estadoPedido = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido.ENTREGADO
      AND p.estadoPago = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago.PAGADO
""")
    Optional<VentasPorRangoResponseDTO> getVentasPorRango(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    @Query("""
    SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.CantidadPedidosPorEstadoResponseDTO(
        p.estadoPedido,
        COUNT(p)
    )
    FROM PedidoEntity p
    WHERE YEAR(p.fecha) = :anio
      AND MONTH(p.fecha) = :mes
    GROUP BY p.estadoPedido
""")
    List<CantidadPedidosPorEstadoResponseDTO> getCantidadPedidosPorEstadoPorMes(
            @Param("anio") Integer anio,
            @Param("mes") Integer mes
    );

    @Query("""
    SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.TicketPromedioResponseDTO(
        :anio,
        :mes,
        COUNT(p),
        SUM(p.montoTotal)
    )
    FROM PedidoEntity p
    WHERE YEAR(p.fecha) = :anio
      AND MONTH(p.fecha) = :mes
      AND p.estadoPedido = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido.ENTREGADO
      AND p.estadoPago = SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago.PAGADO
""")
    TicketPromedioResponseDTO getTicketPromedioVentas(
            @Param("anio") Integer anio,
            @Param("mes") Integer mes
    );



}

