package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

import java.time.LocalDate;

// State: lo que se puede hacer con un pedido depende de su estado.
// Cada estado sabe que operaciones permite y a que estado pasa el pedido.
public interface EstadoPedidoState {

    // true si el pedido tiene mercaderia descontada del stock que habria que devolver al cancelarlo
    boolean tieneStockReservado();

    void confirmarMercaderia(PedidoEntity pedido);

    void programarEntrega(PedidoEntity pedido, LocalDate fechaEntrega);

    void volverACoordinar(PedidoEntity pedido);

    void entregar(PedidoEntity pedido);

    void cancelar(PedidoEntity pedido);

    void validarOrdenReparto(PedidoEntity pedido);

    void validarRecibirPago(PedidoEntity pedido);
}
