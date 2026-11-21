package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Pruebas de cada transicion del patron State, con los mismos mensajes que devolvia la API antes del refactor
class EstadoPedidoStateTest {

    private static final LocalDate MANANA = LocalDate.now().plusDays(1);

    private static PedidoEntity pedido(EstadoPedido estado) {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(7L);
        pedido.setEstadoPedido(estado);
        pedido.setEstadoPago(EstadoPago.PENDIENTE);
        return pedido;
    }

    private static void agregarDetalle(PedidoEntity pedido, int stock, int cantidad) {
        AlimentoEntity producto = new AlimentoEntity();
        producto.setMarca("Dog Chow");
        producto.setNombre("Adulto");
        producto.setStock(stock);

        DetallePedidoEntity detalle = new DetallePedidoEntity();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        pedido.getProductos().add(detalle);
    }

    @ParameterizedTest
    @EnumSource(EstadoPedido.class)
    void cadaValorDelEnumTieneSuClaseDeEstado(EstadoPedido estado) {
        assertThat(pedido(estado).state()).isNotNull();
    }

    @Nested
    class FaltaMercaderia {

        @Test
        void noTieneStockReservado() {
            assertThat(pedido(EstadoPedido.FALTA_MERCADERIA).state().tieneStockReservado()).isFalse();
        }

        @Test
        void confirmarMercaderiaConStockSuficientePasaACoordinar() {
            PedidoEntity pedido = pedido(EstadoPedido.FALTA_MERCADERIA);
            agregarDetalle(pedido, 10, 3);

            pedido.state().confirmarMercaderia(pedido);

            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.A_COORDINAR);
        }

        @Test
        void confirmarMercaderiaSinStockInformaLoQueFalta() {
            PedidoEntity pedido = pedido(EstadoPedido.FALTA_MERCADERIA);
            agregarDetalle(pedido, 1, 3);

            assertThatThrownBy(() -> pedido.state().confirmarMercaderia(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("Todavia no alcanza el stock de: Dog Chow Adulto (hay 1, se necesitan 3)");
            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.FALTA_MERCADERIA);
        }

        @Test
        void noSePuedeProgramar() {
            PedidoEntity pedido = pedido(EstadoPedido.FALTA_MERCADERIA);
            assertThatThrownBy(() -> pedido.state().programarEntrega(pedido, MANANA))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("Solo se puede programar la entrega de un pedido con la mercaderia lista");
        }

        @Test
        void seCancelaYAnulaElPago() {
            PedidoEntity pedido = pedido(EstadoPedido.FALTA_MERCADERIA);

            pedido.state().cancelar(pedido);

            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.CANCELADO);
            assertThat(pedido.getEstadoPago()).isEqualTo(EstadoPago.PEDIDO_CANCELADO);
        }
    }

    @Nested
    class ACoordinar {

        @Test
        void tieneStockReservado() {
            assertThat(pedido(EstadoPedido.A_COORDINAR).state().tieneStockReservado()).isTrue();
        }

        @Test
        void programarEntregaPasaAProgramado() {
            PedidoEntity pedido = pedido(EstadoPedido.A_COORDINAR);

            pedido.state().programarEntrega(pedido, MANANA);

            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.PROGRAMADO);
            assertThat(pedido.getFechaEntrega()).isEqualTo(MANANA);
        }

        @Test
        void noEstaEsperandoMercaderia() {
            PedidoEntity pedido = pedido(EstadoPedido.A_COORDINAR);
            assertThatThrownBy(() -> pedido.state().confirmarMercaderia(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("Este pedido no esta esperando mercaderia");
        }

        @Test
        void noSePuedeEntregarSinFecha() {
            PedidoEntity pedido = pedido(EstadoPedido.A_COORDINAR);
            assertThatThrownBy(() -> pedido.state().entregar(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("Solo se pueden entregar pedidos con fecha de entrega programada");
        }

        @Test
        void noSePuedeVolverACoordinar() {
            PedidoEntity pedido = pedido(EstadoPedido.A_COORDINAR);
            assertThatThrownBy(() -> pedido.state().volverACoordinar(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("Solo se puede volver a coordinar un pedido programado");
        }

        @Test
        void noEntraEnElOrdenDeReparto() {
            PedidoEntity pedido = pedido(EstadoPedido.A_COORDINAR);
            assertThatThrownBy(() -> pedido.state().validarOrdenReparto(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("El pedido #7 no esta programado para entregar");
        }

        @Test
        void noSeCancelaSiTienePagos() {
            PedidoEntity pedido = pedido(EstadoPedido.A_COORDINAR);
            pedido.getPagos().add(new PagoEntity());

            assertThatThrownBy(() -> pedido.state().cancelar(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("No se puede cancelar un pedido que ya tiene pagos registrados");
            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.A_COORDINAR);
        }
    }

    @Nested
    class Programado {

        @Test
        void entregarPasaAEntregado() {
            PedidoEntity pedido = pedido(EstadoPedido.PROGRAMADO);

            pedido.state().entregar(pedido);

            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.ENTREGADO);
        }

        @Test
        void volverACoordinarBorraFechaYOrden() {
            PedidoEntity pedido = pedido(EstadoPedido.PROGRAMADO);
            pedido.setFechaEntrega(MANANA);
            pedido.setOrdenReparto(2);

            pedido.state().volverACoordinar(pedido);

            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.A_COORDINAR);
            assertThat(pedido.getFechaEntrega()).isNull();
            assertThat(pedido.getOrdenReparto()).isNull();
        }

        @Test
        void reprogramarElMismoDiaConservaElOrden() {
            PedidoEntity pedido = pedido(EstadoPedido.PROGRAMADO);
            pedido.setFechaEntrega(MANANA);
            pedido.setOrdenReparto(2);

            pedido.state().programarEntrega(pedido, MANANA);

            assertThat(pedido.getOrdenReparto()).isEqualTo(2);
        }

        @Test
        void reprogramarOtroDiaBorraElOrden() {
            PedidoEntity pedido = pedido(EstadoPedido.PROGRAMADO);
            pedido.setFechaEntrega(MANANA);
            pedido.setOrdenReparto(2);

            pedido.state().programarEntrega(pedido, MANANA.plusDays(1));

            assertThat(pedido.getFechaEntrega()).isEqualTo(MANANA.plusDays(1));
            assertThat(pedido.getOrdenReparto()).isNull();
        }

        @Test
        void entraEnElOrdenDeReparto() {
            PedidoEntity pedido = pedido(EstadoPedido.PROGRAMADO);
            pedido.state().validarOrdenReparto(pedido);
        }

        @Test
        void tieneStockReservadoYSePuedeCancelar() {
            PedidoEntity pedido = pedido(EstadoPedido.PROGRAMADO);
            assertThat(pedido.state().tieneStockReservado()).isTrue();

            pedido.state().cancelar(pedido);

            assertThat(pedido.getEstadoPedido()).isEqualTo(EstadoPedido.CANCELADO);
        }
    }

    @Nested
    class Entregado {

        @Test
        void noSeEntregaDosVeces() {
            PedidoEntity pedido = pedido(EstadoPedido.ENTREGADO);
            assertThatThrownBy(() -> pedido.state().entregar(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("El pedido ya fue entregado");
        }

        @Test
        void noSeCancela() {
            PedidoEntity pedido = pedido(EstadoPedido.ENTREGADO);
            assertThatThrownBy(() -> pedido.state().cancelar(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("No se puede cancelar un pedido que ya fue entregado");
        }

        @Test
        void admitePagos() {
            PedidoEntity pedido = pedido(EstadoPedido.ENTREGADO);
            pedido.state().validarRecibirPago(pedido);
        }
    }

    @Nested
    class Cancelado {

        @Test
        void noSeEntrega() {
            PedidoEntity pedido = pedido(EstadoPedido.CANCELADO);
            assertThatThrownBy(() -> pedido.state().entregar(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("No se puede entregar un pedido cancelado");
        }

        @Test
        void noSeCancelaDosVeces() {
            PedidoEntity pedido = pedido(EstadoPedido.CANCELADO);
            assertThatThrownBy(() -> pedido.state().cancelar(pedido))
                    .isInstanceOf(InactiveResourceException.class)
                    .hasMessage("El pedido con la ID: 7 ya fue cancelado");
        }

        @Test
        void noAdmitePagos() {
            PedidoEntity pedido = pedido(EstadoPedido.CANCELADO);
            assertThatThrownBy(() -> pedido.state().validarRecibirPago(pedido))
                    .isInstanceOf(InvalidResourceStateException.class)
                    .hasMessage("No se puede registrar el pago, el pedido esta cancelado");
        }

        @Test
        void noTieneStockReservado() {
            assertThat(pedido(EstadoPedido.CANCELADO).state().tieneStockReservado()).isFalse();
        }
    }
}
