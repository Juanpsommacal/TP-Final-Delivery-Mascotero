package SSVG.TPFinalDeliveryMascotero.Model.Enums;

public enum EstadoPedido {
    // No alcanza el stock de algun producto: todavia no se desconto nada
    FALTA_MERCADERIA,
    // Mercaderia reservada, falta acordar el dia con el cliente
    A_COORDINAR,
    // Tiene fecha de entrega pactada
    PROGRAMADO,
    ENTREGADO,
    CANCELADO
}
