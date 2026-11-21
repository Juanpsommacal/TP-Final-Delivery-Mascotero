package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import SSVG.TPFinalDeliveryMascotero.Service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Eslabon 1: el cliente existe (si no, ClienteService lanza ResourceNotFoundException)
@Component
@RequiredArgsConstructor
public class ClienteExisteHandler extends PedidoValidationHandler {

    private final ClienteService clienteService;

    @Override
    protected void check(PedidoValidationContext contexto) {
        contexto.setCliente(clienteService.getEntityById(contexto.getRequest().getClienteId()));
    }
}
