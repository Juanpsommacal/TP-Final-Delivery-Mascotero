package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import SSVG.TPFinalDeliveryMascotero.Exception.DuplicateResourceException;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.DetallePedido.DetallePedidoCreateRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;

// Eslabon 3: el detalle no repite productos
@Component
public class ProductosRepetidosHandler extends PedidoValidationHandler {

    @Override
    protected void check(PedidoValidationContext contexto) {
        List<DetallePedidoCreateRequestDTO> detalles = contexto.getRequest().getDetalles();

        long cantProductosUnicos = detalles.stream()
                .map(DetallePedidoCreateRequestDTO::getProductoId)
                .distinct()
                .count();

        if (cantProductosUnicos != detalles.size()) {
            throw new DuplicateResourceException("No se puede repetir el mismo Producto en el detalle del pedido");
        }
    }
}
