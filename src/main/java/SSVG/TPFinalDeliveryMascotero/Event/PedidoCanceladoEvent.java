package SSVG.TPFinalDeliveryMascotero.Event;

import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

// Observer: se publica cuando se cancela un pedido.
// reservabaStock dice si el pedido tenia mercaderia descontada antes de cancelarse
// (se calcula antes, porque despues de cancelar el estado ya es CANCELADO).
public record PedidoCanceladoEvent(PedidoEntity pedido, boolean reservabaStock) { }
