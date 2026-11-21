package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

// Estado final: el pedido se cancelo.
// Sobrescribe entregar y cancelar solo para conservar los mensajes y excepciones que ya devolvia la API.
public class CanceladoState extends EstadoPedidoBase {

    @Override
    public void entregar(PedidoEntity pedido) {
        throw new InvalidResourceStateException("No se puede entregar un pedido cancelado");
    }

    @Override
    public void cancelar(PedidoEntity pedido) {
        throw new InactiveResourceException("El pedido con la ID: " + pedido.getId() + " ya fue cancelado");
    }

    @Override
    public void validarRecibirPago(PedidoEntity pedido) {
        throw new InvalidResourceStateException("No se puede registrar el pago, el pedido esta cancelado");
    }
}
