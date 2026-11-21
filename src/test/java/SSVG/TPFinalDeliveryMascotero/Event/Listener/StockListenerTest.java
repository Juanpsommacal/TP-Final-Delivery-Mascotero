package SSVG.TPFinalDeliveryMascotero.Event.Listener;

import SSVG.TPFinalDeliveryMascotero.Event.PedidoCanceladoEvent;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Service.ProductoService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Pruebas del observador de stock del patron Observer
class StockListenerTest {

    private final ProductoService productoService = mock(ProductoService.class);
    private final StockListener listener = new StockListener(productoService);

    private PedidoEntity pedidoCon(AlimentoEntity producto, int cantidad) {
        DetallePedidoEntity detalle = new DetallePedidoEntity();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        PedidoEntity pedido = new PedidoEntity();
        pedido.getProductos().add(detalle);
        return pedido;
    }

    private AlimentoEntity producto(int stock) {
        AlimentoEntity producto = new AlimentoEntity();
        producto.setId(5L);
        producto.setStock(stock);
        when(productoService.getEntityById(5L)).thenReturn(producto);
        return producto;
    }

    @Test
    void devuelveElStockSiElPedidoLoTeniaReservado() {
        AlimentoEntity producto = producto(4);

        listener.alCancelarPedido(new PedidoCanceladoEvent(pedidoCon(producto, 3), true));

        assertThat(producto.getStock()).isEqualTo(7);
        verify(productoService).saveEntity(producto);
    }

    @Test
    void noTocaElStockSiFaltabaMercaderia() {
        AlimentoEntity producto = producto(4);

        listener.alCancelarPedido(new PedidoCanceladoEvent(pedidoCon(producto, 3), false));

        assertThat(producto.getStock()).isEqualTo(4);
        verifyNoInteractions(productoService);
    }
}
