package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotAssociatedException;
import SSVG.TPFinalDeliveryMascotero.Mapper.DireccionMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Eslabon 2: la direccion de entrega es una de las del cliente
@Component
@RequiredArgsConstructor
public class DireccionDelClienteHandler extends PedidoValidationHandler {

    private final ClienteService clienteService;
    private final DireccionMapper direccionMapper;

    @Override
    protected void check(PedidoValidationContext contexto) {
        DireccionEntity direccion = direccionMapper.toEntity(contexto.getRequest().getDireccion());
        if (!clienteService.hasDireccion(contexto.getCliente(), direccion)) {
            throw new ResourceNotAssociatedException("El cliente no tiene esa direccion asociada");
        }
        contexto.setDireccion(direccion);
    }
}
