package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

import java.time.LocalDate;

// Mercaderia reservada, falta acordar el dia con el cliente
public class ACoordinarState extends EstadoPedidoBase {

    @Override
    public boolean tieneStockReservado() {
        return true;
    }

    @Override
    public void programarEntrega(PedidoEntity pedido, LocalDate fechaEntrega) {
        programar(pedido, fechaEntrega);
    }

    @Override
    public void cancelar(PedidoEntity pedido) {
        cancelarSinPagos(pedido);
    }
}
