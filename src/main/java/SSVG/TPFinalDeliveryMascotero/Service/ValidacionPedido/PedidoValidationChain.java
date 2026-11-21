package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import org.springframework.stereotype.Component;

// Arma la cadena de validaciones de un pedido nuevo. El orden se define aca, en un solo lugar,
// y es el mismo que tenia createPedido, asi los errores salen en la misma secuencia.
@Component
public class PedidoValidationChain {

    private final PedidoValidationHandler first;

    public PedidoValidationChain(ClienteExisteHandler cliente,
                                 DireccionDelClienteHandler direccion,
                                 ProductosRepetidosHandler repetidos,
                                 ProductosActivosHandler activos) {
        cliente.setNext(direccion).setNext(repetidos).setNext(activos);
        this.first = cliente;
    }

    public void validar(PedidoValidationContext contexto) {
        first.handle(contexto);
    }
}
