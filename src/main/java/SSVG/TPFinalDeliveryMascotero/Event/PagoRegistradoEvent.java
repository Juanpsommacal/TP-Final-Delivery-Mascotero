package SSVG.TPFinalDeliveryMascotero.Event;

import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

// Observer: se publica cuando se registra un pago de un pedido
public record PagoRegistradoEvent(PedidoEntity pedido) { }
