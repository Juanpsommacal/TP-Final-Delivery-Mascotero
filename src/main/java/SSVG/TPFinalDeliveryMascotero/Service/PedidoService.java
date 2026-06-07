package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Controller.ProductoController;
import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InsufficientStockException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotAssociatedException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.PedidoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final DireccionService direccionService;
    private final PedidoMapper mapper;

    @Transactional
    public PedidoResponseDTO createPedido(PedidoCreateRequestDTO request){

        //Buscamos si el cliente existe
        ClienteEntity cliente = clienteService.getEntityById(request.getClienteId());

        //Buscamos si la direccion existe
        DireccionEntity direccion = direccionService.getEntityById(request.getDireccionId());

        //Verificamos si el cliente tiene esa direccion asociada
        if(!clienteService.isAssociated(cliente, direccion.getId()))
            throw new ResourceNotAssociatedException("El cliente no tiene esa direccion asociada");

        //Ahora verificamos si el stock que tenemos es suficiente para el pedido
        Map<String, String> errorsMap = new HashMap<>();
        Map<String, String> inactiveMap = new HashMap<>();
        request.getDetalles()
                .forEach(detalle -> {

                    ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());

                    //Si hay algun producto que no tenga stock lo guardamos en errorsMap
                    if(producto.getStock() < detalle.getCantidad())
                        errorsMap.put(producto.getMarca().concat(producto.getNombre()),
                                    "| Stock necesario: "
                                    + detalle.getCantidad()
                                    + "| Stock disponible: "
                                    + producto.getStock());
                    //Ahora verificamos que no haya ningun producto inactivo
                    if(!producto.getActivo())
                        errorsMap.put(producto.getMarca().concat(producto.getNombre()),
                                "| Producto inactivo");
                });

        //Revisamos si errorsMap tiene contenido. Si hay errores tiramos la excepcion
        if(!errorsMap.isEmpty())
            throw new InsufficientStockException(errorsMap);


        //Sino creamos el pedido vacio y vamos seteando los atributos
        PedidoEntity newPedido = new PedidoEntity();

        newPedido.setCliente(cliente);
        newPedido.setFecha(LocalDate.now());
        newPedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        newPedido.setEstadoPago(EstadoPago.PENDIENTE);

        //Calculamos el precio total del pedido
        BigDecimal montoTotal = request.getDetalles()
                .stream()
                .map(detalle ->
                        productoService.getEntityById(detalle.getProductoId())
                                .getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                //acumulamos para obtener el total
                .reduce(BigDecimal.ZERO,
                        BigDecimal::add);

        //Descontamos el stock de los productos
        request.getDetalles()
                .forEach(detalle -> {
                    ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());
                    producto.setStock(producto.getStock() - detalle.getCantidad());
                    productoService.saveEntity(producto);
                });

        //Seteamos el precio
        newPedido.setMontoTotal(montoTotal);

        //Guardamos el pedido asi nos da la ID
        PedidoEntity savedPedido = repository.save(newPedido);

        //Ahora creamos la lista de detallePedidoEntity
        List<DetallePedidoEntity> detalles = request.getDetalles()
                .stream()
                //Transformamos a producto
                .map(requestDetalle -> {

                    ProductoEntity producto = productoService.getEntityById(requestDetalle.getProductoId());

                    //Creamos los detallePedidoEntity y seteamos los atributos
                    DetallePedidoEntity newDetalle = new DetallePedidoEntity();
                    newDetalle.setCantidad(requestDetalle.getCantidad());
                    newDetalle.setPrecioUnitario(producto.getPrecio());
                    ///ACA IRIA LA LOGICA PARA VER SI TIENE OFERTA EL PRODUCTO
                    newDetalle.setPedido(savedPedido);
                    newDetalle.setProducto(producto);
                    return newDetalle;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        //Seteamos el detalle dentro del pedido
        savedPedido.setProductos(detalles);

        //Guardamos en el repo y devolvemos el DTO de response
        return mapper.toResponse(repository.save(savedPedido));
    }

    public PedidoEntity getEntityById(Long id){
        Optional<PedidoEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("El pedido no existe");
    }

    public PedidoResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<PedidoResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
