package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

import java.util.List;

// No alcanzaba el stock al tomar el pedido: todavia no se desconto nada
public class FaltaMercaderiaState extends EstadoPedidoBase {

    // Llego la mercaderia: si ya alcanza para todo el pedido, queda listo para coordinar.
    // El descuento del stock lo hace PedidoService despues de esta transicion.
    @Override
    public void confirmarMercaderia(PedidoEntity pedido) {
        List<String> faltantes = pedido.getProductos().stream()
                .filter(detalle -> detalle.getProducto().getStock() < detalle.getCantidad())
                .map(detalle -> detalle.getProducto().getMarca() + " " + detalle.getProducto().getNombre()
                        + " (hay " + detalle.getProducto().getStock() + ", se necesitan " + detalle.getCantidad() + ")")
                .toList();
        if (!faltantes.isEmpty()) {
            throw new InvalidResourceStateException("Todavia no alcanza el stock de: " + String.join(", ", faltantes));
        }
        pedido.setEstadoPedido(EstadoPedido.A_COORDINAR);
    }

    @Override
    public void cancelar(PedidoEntity pedido) {
        cancelarSinPagos(pedido);
    }
}
