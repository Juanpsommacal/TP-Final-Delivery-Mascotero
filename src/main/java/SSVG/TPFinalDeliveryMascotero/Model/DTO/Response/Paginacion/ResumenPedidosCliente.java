package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumenPedidosCliente(Long clienteId, Long pedidos, LocalDate ultimoPedido, Long ultimoPedidoId, BigDecimal deuda) {}
