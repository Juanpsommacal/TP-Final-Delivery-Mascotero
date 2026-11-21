package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import SSVG.TPFinalDeliveryMascotero.Exception.InsufficientStockException;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Eslabon 4: todos los productos existen y estan activos.
// Deja los productos buscados en el contexto para que createPedido no los vuelva a buscar.
@Component
@RequiredArgsConstructor
public class ProductosActivosHandler extends PedidoValidationHandler {

    private final ProductoService productoService;

    @Override
    protected void check(PedidoValidationContext contexto) {
        List<String> productosInactivos = new ArrayList<>();

        contexto.getRequest().getDetalles().forEach(detalle -> {
            ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());
            contexto.getProductos().put(producto.getId(), producto);

            if (!producto.getActivo()) {
                productosInactivos.add(producto.getMarca().concat(" ").concat(producto.getNombre()));
            }
        });

        // Se informa con InsufficientStockException para no cambiar la respuesta de la API
        if (!productosInactivos.isEmpty()) {
            Map<String, List<String>> errorsMap = new HashMap<>();
            errorsMap.put("Productos inactivos: ", productosInactivos);
            throw new InsufficientStockException(errorsMap);
        }
    }
}
