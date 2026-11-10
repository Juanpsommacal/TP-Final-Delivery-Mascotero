package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/// Fila del listado de clientes: el cliente y el resumen de sus pedidos no cancelados.
public record ClienteFilaResponseDTO(ClienteResponseDTO cliente, long pedidos, BigDecimal deuda,
                                     LocalDate ultimoPedido, Long ultimoPedidoId) {}
