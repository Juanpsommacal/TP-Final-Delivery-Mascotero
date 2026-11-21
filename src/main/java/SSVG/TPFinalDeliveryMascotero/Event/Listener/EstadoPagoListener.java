package SSVG.TPFinalDeliveryMascotero.Event.Listener;

import SSVG.TPFinalDeliveryMascotero.Event.PagoRegistradoEvent;
import SSVG.TPFinalDeliveryMascotero.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Observador: cuando se registra un pago, recalcula el estado de pago del pedido.
// Usa la misma regla que deliverPedido, que sigue definida en un solo lugar (PedidoService).
// @EventListener corre en el mismo hilo y en la misma transaccion que quien publica.
@Component
@RequiredArgsConstructor
public class EstadoPagoListener {

    private final PedidoService pedidoService;

    @EventListener
    public void alRegistrarPago(PagoRegistradoEvent event) {
        pedidoService.actualizarEstadoPago(event.pedido());
    }
}
