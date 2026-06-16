package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.DuplicateResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.CompraMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.DetalleCompraMapper;
import SSVG.TPFinalDeliveryMascotero.Model.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.CompraCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.CompraResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetalleCompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoCompra;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.ProveedorEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.CompraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompraService {

    private final CompraRepository repository;
    private final CompraMapper mapper;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final DetalleCompraMapper detalleCompraMapper;

    @Transactional
    public CompraResponseDTO createCompra(CompraCreateRequestDTO request){

        validateNotRepeatProduct(request);

        //Buscamos el proveedor
        ProveedorEntity proveedor = proveedorService.getEntityById(request.getProveedorId());

        //Creamos la newCompra vacia y vamos seteando los atributos
        CompraEntity newCompra = mapper.toEntity(request);

        newCompra.setProveedor(proveedor);
        newCompra.setFecha(LocalDate.now());
        newCompra.setEstadoCompra(EstadoCompra.PENDIENTE);

        //Para calcular el monto total del pedido multiplicamos el precio de la request por la cantidad
        BigDecimal montoTotal = request.getDetalle().stream()
                .map(detalle ->
                        detalle.getPrecioUnitario()
                                .multiply(BigDecimal.valueOf(detalle.getCantidad())))
                //Eso lo acumulamos y lo vamos sumando
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Ahora seteamos el precio
        newCompra.setMontoTotal(montoTotal);

        //Ahora creamos una lista de detalleCompraEntity
        List<DetalleCompraEntity> detalles = request.getDetalle()
                .stream()
                //Transformamos a producto
                .map(requestDetalle -> {

                    ProductoEntity producto = productoService.getEntityById(requestDetalle.getProductoId());

                    //Creamos los detalleCompraEntity y seteamos los atributos
                    DetalleCompraEntity newDetalle = detalleCompraMapper.toEntity(requestDetalle);
                    newDetalle.setProducto(producto);
                    newDetalle.setCompra(newCompra);

                    return newDetalle;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        //Seteamos el detalle dentro de la newCompra
        newCompra.setProductos(detalles);

        //Y guardamos la newCompra en el Repository de las Compras
        CompraEntity savedCompra = repository.save(newCompra);

        // Devolvemos el DTO de response.
        return mapper.toResponse(savedCompra);
    }

    public CompraEntity getEntityById(Long id){
        Optional<CompraEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("La compra con la ID: " + id + " no existe");
    }

    public CompraResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<CompraResponseDTO>getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CompraResponseDTO receiveCompra(Long id){
        // Se valida que exista la compra
        CompraEntity compra = getEntityById(id);

        // Se hacen las validaciones del Estado de la Compra
        if (compra.getEstadoCompra() == EstadoCompra.RECIBIDA){
            throw new InvalidResourceStateException("La compra ya fue recibida");
        }
        if (compra.getEstadoCompra() == EstadoCompra.CANCELADA){
            throw new InvalidResourceStateException("No se puede recibir una compra cancelada");
        }

        // Aca se recorren los detalles de la compra y se le hace la suma al producto, de la cantidad recibida por la compra
        compra.getProductos().forEach(detalleCompra ->{
            // Se le actualiza el Stock a los productos del detalle de la compra
            increaseProductStock(detalleCompra.getProducto(), detalleCompra.getCantidad());
        });

        // Por ultimo, se actualiza el estado de la compra a RECIBIDA
        compra.setEstadoCompra(EstadoCompra.RECIBIDA);

        return mapper.toResponse(repository.save(compra));
    }

    // Funciones Utiles

    // Sirve para validar que no se cargue el mismo producto 2 veces en la misma request tanto de Compra, Pedido y Oferta
    private void validateNotRepeatProduct(CompraCreateRequestDTO request) {

        // Se obtienen la cantidad de Productos Unicos de la request eliminando los repetidos
        // para despues comparar con los que hay realmente en la request
        long cantProductosUnicos = request.getDetalle().stream()
                .map(detalle -> detalle.getProductoId())
                .distinct()
                .count();

        // Si "cantProductosUnicos" = 2 y en la request original (sin eliminar los repetidos) habian 3 productoId
        // tira excepcion que se repitieron los productos en la request
        if (cantProductosUnicos != request.getDetalle().size()){
            throw new DuplicateResourceException("No se puede repetir el mismo Producto en el detalle de la compra");
        }
    }

    // Aumenta el Stock del Producto, si se recibe "null" se setea a 0
    private void increaseProductStock(ProductoEntity producto, Integer cantidad){
        Integer stockActual = producto.getStock();

        if (stockActual == null){
            stockActual = 0;
        }
        producto.setStock(stockActual + cantidad);
    }
}
