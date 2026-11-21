package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

import java.time.LocalDate;

// Tiene fecha de entrega pactada
public class ProgramadoState extends EstadoPedidoBase {

    @Override
    public boolean tieneStockReservado() {
        return true;
    }

    // Sirve tambien para cambiar el dia ya pactado
    @Override
    public void programarEntrega(PedidoEntity pedido, LocalDate fechaEntrega) {
        programar(pedido, fechaEntrega);
    }

    // El cliente no puede el dia pactado y todavia no hay otro
    @Override
    public void volverACoordinar(PedidoEntity pedido) {
        pedido.setFechaEntrega(null);
        pedido.setOrdenReparto(null);
        pedido.setEstadoPedido(EstadoPedido.A_COORDINAR);
    }

    @Override
    public void entregar(PedidoEntity pedido) {
        pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
    }

    @Override
    public void cancelar(PedidoEntity pedido) {
        cancelarSinPagos(pedido);
    }

    @Override
    public void validarOrdenReparto(PedidoEntity pedido) {
        // Solo los programados entran en el recorrido del reparto
    }
}
