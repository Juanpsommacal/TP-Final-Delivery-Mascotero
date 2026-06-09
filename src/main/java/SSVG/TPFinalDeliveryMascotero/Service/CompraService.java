package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.CompraMapper;
import SSVG.TPFinalDeliveryMascotero.Model.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.CompraCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.CompraResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetalleCompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.ProveedorEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.CompraRepository;
import SSVG.TPFinalDeliveryMascotero.Repository.DetalleCompraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompraService {

    private final CompraRepository repository;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final CompraMapper mapper;

    @Transactional
    public CompraResponseDTO createCompra(CompraCreateRequestDTO request){

        //Buscamos el proveedor
        ProveedorEntity proveedor = proveedorService.getEntityById(request.getProveedorId());

        //Creamos la compra vacia y vamos seteando los atributos
        CompraEntity newCompra = new CompraEntity();

        newCompra.setProveedor(proveedor);
        newCompra.setFecha(LocalDate.now());
        newCompra.setEstado(EstadoPedido.PENDIENTE);

        //Para calcular el monto total del pedido multiplicamos el precio de la request por la cantidad
        BigDecimal montoTotal = request.getDetalle()
                .stream()
                .map(detalle ->
                        detalle.getPrecioUnitario()
                                .multiply(BigDecimal.valueOf(detalle.getCantidad())))
                //Eso lo acumulamos y lo vamos sumando
                .reduce(BigDecimal.ZERO,
                        BigDecimal::add);

        //Ahora seteamos el precio
        newCompra.setMontoTotal(montoTotal);

        //Y guardamos la compra asi nos da la ID
        CompraEntity savedCompra = repository.save(newCompra);

        //Ahora creamos una lista de detalleCompraEntity
        List<DetalleCompraEntity> detalles = request.getDetalle()
                .stream()
                //Transformamos a producto
                .map(requestDetalle -> {

                    ProductoEntity producto =
                            productoService.getEntityById(requestDetalle.getProductoId());

                    // Se le aumenta el Stock del Producto (stockActual + la cantidad ingresada)
                    increaseProductStock(producto, requestDetalle.getCantidad());

                    //Creamos los detalleCompraEntity y seteamos los atributos
                    DetalleCompraEntity newDetalle = new DetalleCompraEntity();
                    newDetalle.setCompra(savedCompra);
                    newDetalle.setProducto(producto);
                    newDetalle.setCantidad(requestDetalle.getCantidad());
                    newDetalle.setPrecioUnitario(requestDetalle.getPrecioUnitario());

                    return newDetalle;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        //Seteamos el detalle dentro de la compra
        savedCompra.setProductos(detalles);

        //Guardamos en el repo y devolvemos el DTO de response.
        return mapper.toResponse(repository.save(savedCompra));
    }

    public CompraEntity getEntityById(Long id){
        Optional<CompraEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("La compra no existe");
    }

    public CompraResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<CompraResponseDTO>getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    // Funciones Utiles

    private void increaseProductStock(ProductoEntity producto, Integer cantidad){
        Integer stockActual = producto.getStock();

        if (stockActual == null){
            stockActual = 0;
        }
        producto.setStock(stockActual + cantidad);
    }
}
