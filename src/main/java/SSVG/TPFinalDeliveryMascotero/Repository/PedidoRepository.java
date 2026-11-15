package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.ResumenPedidosCliente;
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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long>, JpaSpecificationExecutor<PedidoEntity> {

    List<PedidoEntity> findByCalleIgnoreCaseAndNumero(String calle, Integer numero);

    List<PedidoEntity> findByFecha(LocalDate fecha);

    List<PedidoEntity> findByEstadoPedido(EstadoPedido estado);

    List<PedidoEntity> findByEstadoPago(EstadoPago estado);

    boolean existsByClienteId(Long clienteId);

    List<PedidoEntity> findByEstadoPedidoAndFechaEntrega(EstadoPedido estado, LocalDate fechaEntrega);

    List<PedidoEntity> findByEstadoPedidoAndFechaEntregaBefore(EstadoPedido estado, LocalDate fechaEntrega);

    @Query("SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.CantidadPedidosPorEstadoResponseDTO(p.estadoPedido, COUNT(p)) FROM PedidoEntity p GROUP BY p.estadoPedido")
    List<CantidadPedidosPorEstadoResponseDTO> getCantidadPorEstado();

    List<PedidoEntity> findByClienteId(Long clienteId);

    @Query("""
    SELECT new SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.ResumenPedidosCliente(
        p.cliente.id,
        COUNT(p),
        MAX(p.fecha),
        MAX(p.id),
        COALESCE(SUM(CASE
            WHEN p.estadoPago IN (SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago.DEUDA,
                                  SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago.DEUDA_PARCIAL)
            THEN p.montoTotal - COALESCE((SELECT SUM(pago.monto) FROM PagoEntity pago WHERE pago.pedido = p), 0)
            ELSE 0
        END), 0)
    )
    FROM PedidoEntity p
    WHERE p.cliente.id IN :clienteIds
      AND p.estadoPedido <> SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido.CANCELADO
    GROUP BY p.cliente.id
""")
    List<ResumenPedidosCliente> getResumenPorCliente(@Param("clienteIds") List<Long> clienteIds);

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

