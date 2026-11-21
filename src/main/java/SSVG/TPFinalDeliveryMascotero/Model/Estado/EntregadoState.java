package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

// Estado final: el pedido ya se entrego.
// Sobrescribe entregar y cancelar solo para conservar los mensajes que ya devolvia la API.
public class EntregadoState extends EstadoPedidoBase {

    @Override
    public void entregar(PedidoEntity pedido) {
        throw new InvalidResourceStateException("El pedido ya fue entregado");
    }

    @Override
    public void cancelar(PedidoEntity pedido) {
        throw new InvalidResourceStateException("No se puede cancelar un pedido que ya fue entregado");
    }
}
