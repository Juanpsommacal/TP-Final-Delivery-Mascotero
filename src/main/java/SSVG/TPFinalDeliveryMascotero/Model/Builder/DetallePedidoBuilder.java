package SSVG.TPFinalDeliveryMascotero.Model.Builder;

import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;

import java.math.BigDecimal;

// Builder: arma un renglon del pedido. build() valida los datos obligatorios;
// si el producto no tenia oferta vigente, el descuento aplicado queda en 0.
public class DetallePedidoBuilder {

    private ProductoEntity producto;
    private Integer cantidad;
    private BigDecimal precioOriginal;
    private BigDecimal precioConDescuento;
    private Double descuentoAplicado = 0.0;
    private PedidoEntity pedido;

    public DetallePedidoBuilder deProducto(ProductoEntity producto) {
        this.producto = producto;
        return this;
    }

    public DetallePedidoBuilder cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public DetallePedidoBuilder precioOriginal(BigDecimal precioOriginal) {
        this.precioOriginal = precioOriginal;
        return this;
    }

    public DetallePedidoBuilder precioConDescuento(BigDecimal precioConDescuento) {
        this.precioConDescuento = precioConDescuento;
        return this;
    }

    public DetallePedidoBuilder descuentoAplicado(Double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
        return this;
    }

    public DetallePedidoBuilder delPedido(PedidoEntity pedido) {
        this.pedido = pedido;
        return this;
    }

    public DetallePedidoEntity build() {
        if (producto == null || cantidad == null || precioOriginal == null
                || precioConDescuento == null || pedido == null)
            throw new IllegalStateException("Faltan datos obligatorios del detalle del pedido");

        DetallePedidoEntity detalle = new DetallePedidoEntity();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioOriginal);
        detalle.setPrecioDescuento(precioConDescuento);
        detalle.setDescuentoAplicado(descuentoAplicado);
        detalle.setPedido(pedido);
        return detalle;
    }
}
