package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

// Lo que recorre la cadena: la request y los datos que los eslabones van resolviendo,
// para que createPedido no tenga que volver a buscarlos.
@Getter
@Setter
public class PedidoValidationContext {

    private final PedidoCreateRequestDTO request;
    private ClienteEntity cliente;
    private DireccionEntity direccion;
    // Productos del detalle ya buscados, por id
    private final Map<Long, ProductoEntity> productos = new LinkedHashMap<>();

    public PedidoValidationContext(PedidoCreateRequestDTO request) {
        this.request = request;
    }

    public ProductoEntity getProducto(Long id) {
        return productos.get(id);
    }
}
