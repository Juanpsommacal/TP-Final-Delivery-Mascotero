package SSVG.TPFinalDeliveryMascotero.Model.Estado;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;

import java.util.EnumMap;
import java.util.Map;

// Asocia cada valor del enum (lo que se guarda en la base) con su clase de estado.
// Los estados no guardan datos, asi que alcanza con una instancia de cada uno.
public final class EstadoPedidoStates {

    private static final Map<EstadoPedido, EstadoPedidoState> ESTADOS = new EnumMap<>(EstadoPedido.class);

    static {
        ESTADOS.put(EstadoPedido.FALTA_MERCADERIA, new FaltaMercaderiaState());
        ESTADOS.put(EstadoPedido.A_COORDINAR, new ACoordinarState());
        ESTADOS.put(EstadoPedido.PROGRAMADO, new ProgramadoState());
        ESTADOS.put(EstadoPedido.ENTREGADO, new EntregadoState());
        ESTADOS.put(EstadoPedido.CANCELADO, new CanceladoState());
    }

    private EstadoPedidoStates() { }

    public static EstadoPedidoState de(EstadoPedido estado) {
        EstadoPedidoState state = ESTADOS.get(estado);
        if (state == null) {
            throw new IllegalStateException("No hay una clase de estado para " + estado);
        }
        return state;
    }
}
