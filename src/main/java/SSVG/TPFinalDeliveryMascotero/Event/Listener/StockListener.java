package SSVG.TPFinalDeliveryMascotero.Event.Listener;

import SSVG.TPFinalDeliveryMascotero.Event.PedidoCanceladoEvent;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Observador: cuando se cancela un pedido que tenia mercaderia reservada, devuelve el stock.
// @EventListener corre en el mismo hilo y en la misma transaccion que quien publica:
// si algo falla aca, se revierte tambien la cancelacion.
@Component
@RequiredArgsConstructor
public class StockListener {

    private final ProductoService productoService;

    @EventListener
    public void alCancelarPedido(PedidoCanceladoEvent event) {
        // Si faltaba mercaderia nunca se desconto stock, asi que no hay nada que devolver
        if (!event.reservabaStock()) {
            return;
        }

        event.pedido().getProductos().forEach(detalle -> {
            ProductoEntity producto = productoService.getEntityById(detalle.getProducto().getId());
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoService.saveEntity(producto);
        });
    }
}
