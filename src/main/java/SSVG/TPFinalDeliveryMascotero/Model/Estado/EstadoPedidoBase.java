package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

import java.time.LocalDate;

// Por defecto un estado rechaza las transiciones, con los mismos mensajes que tenia PedidoService.
// Cada estado concreto sobrescribe solo lo que si permite.
// Recibir pagos es la excepcion: se permite por defecto y solo lo rechaza CanceladoState.
public abstract class EstadoPedidoBase implements EstadoPedidoState {

    @Override
    public boolean tieneStockReservado() {
        return false;
    }

    @Override
    public void confirmarMercaderia(PedidoEntity pedido) {
        throw new InvalidResourceStateException("Este pedido no esta esperando mercaderia");
    }

    @Override
    public void programarEntrega(PedidoEntity pedido, LocalDate fechaEntrega) {
        throw new InvalidResourceStateException("Solo se puede programar la entrega de un pedido con la mercaderia lista");
    }

    @Override
    public void volverACoordinar(PedidoEntity pedido) {
        throw new InvalidResourceStateException("Solo se puede volver a coordinar un pedido programado");
    }

    @Override
    public void entregar(PedidoEntity pedido) {
        throw new InvalidResourceStateException("Solo se pueden entregar pedidos con fecha de entrega programada");
    }

    @Override
    public void cancelar(PedidoEntity pedido) {
        throw new InvalidResourceStateException("No se puede cancelar un pedido en estado " + pedido.getEstadoPedido());
    }

    @Override
    public void validarOrdenReparto(PedidoEntity pedido) {
        throw new InvalidResourceStateException("El pedido #" + pedido.getId() + " no esta programado para entregar");
    }

    @Override
    public void validarRecibirPago(PedidoEntity pedido) {
        // Se permite
    }

    ///----- Transiciones que comparten varios estados -----

    // Pacta el dia de entrega. En otro dia el recorrido es otro: vuelve a quedar sin ordenar
    protected void programar(PedidoEntity pedido, LocalDate fechaEntrega) {
        if (!fechaEntrega.equals(pedido.getFechaEntrega())) {
            pedido.setOrdenReparto(null);
        }
        pedido.setFechaEntrega(fechaEntrega);
        pedido.setEstadoPedido(EstadoPedido.PROGRAMADO);
    }

    // Si se cancelara un pedido con pagos, esa plata quedaria sin pedido
    protected void cancelarSinPagos(PedidoEntity pedido) {
        if (!pedido.getPagos().isEmpty()) {
            throw new InvalidResourceStateException("No se puede cancelar un pedido que ya tiene pagos registrados");
        }
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        pedido.setEstadoPago(EstadoPago.PEDIDO_CANCELADO);
    }
}
