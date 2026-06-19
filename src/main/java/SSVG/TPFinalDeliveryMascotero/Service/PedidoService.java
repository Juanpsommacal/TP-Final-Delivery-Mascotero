package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.*;
import SSVG.TPFinalDeliveryMascotero.Exception.*;
import SSVG.TPFinalDeliveryMascotero.Mapper.DireccionMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.PedidoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.CantidadPedidosPorEstadoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.TicketPromedioResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.VentasPorMesResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.VentasPorRangoResponseDTO;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final DireccionMapper direccionMapper;
    private final PedidoMapper mapper;

    @Transactional
    public PedidoResponseDTO createPedido(PedidoCreateRequestDTO request){

        //Buscamos si el cliente existe
        ClienteEntity cliente = clienteService.getEntityById(request.getClienteId());

        //Buscamos si el cliente tiene esa direccion
        DireccionEntity direccionRequest = direccionMapper.toEntity(request.getDireccion());
        if(!clienteService.hasDireccion(cliente, direccionRequest))
            throw new ResourceNotAssociatedException("El cliente no tiene esa direccion asociada");

        // Validamos que no se reciban productos repetidos en la request
        validateNotRepeatProduct(request);

        //Ahora verificamos si el stock que tenemos es suficiente para el pedido
        Map<String, List<String>> errorsMap = new HashMap<>();
        List<String> productosSinStock = new ArrayList<>();
        List<String> productosInactivos = new ArrayList<>();

        request.getDetalles()
                .forEach(detalle -> {

                    ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());

                    //Si hay algun producto que no tenga stock lo guardamos en errorsMap
                    if(producto.getStock() < detalle.getCantidad()){
                        productosSinStock.add(producto.getMarca().concat(" ").concat(producto.getNombre()) +
                                " | Stock necesario: "
                                + detalle.getCantidad()
                                + " | Stock disponible: "
                                + producto.getStock());
                    }
                    //Ahora verificamos que no haya ningun producto inactivo
                    if(!producto.getActivo()){
                        productosInactivos.add(producto.getMarca().concat(" ").concat(producto.getNombre()));
                    }
                });

        errorsMap.put("Productos sin stock: ", productosSinStock);
        errorsMap.put("Productos inactivos: ", productosInactivos);

        //Revisamos si errorsMap tiene contenido. Si hay errores tiramos la excepcion
        if(!errorsMap.get("Productos sin stock: ").isEmpty() || !errorsMap.get("Productos inactivos: ").isEmpty()){
            throw new InsufficientStockException(errorsMap);
        }

        //Creamos el pedido vacio y vamos seteando los atributos
        PedidoEntity newPedido = new PedidoEntity();

        newPedido.setCliente(cliente);
        newPedido.setFecha(LocalDate.now());
        newPedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        newPedido.setEstadoPago(EstadoPago.PENDIENTE);
        newPedido.setCalle(direccionRequest.getCalle());
        newPedido.setNumero(direccionRequest.getNumero());
        newPedido.setPisoDepto(formatearPisoDepto(direccionRequest));

        //Calculamos el precio total del pedido con las ofertas activas
        BigDecimal montoTotal = request.getDetalles()
                .stream()
                .map(detalle -> {
                        ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());

                        BigDecimal precioConDescuento = calculateUnitPriceProducto(producto);

                        return precioConDescuento.multiply(BigDecimal.valueOf(detalle.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

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

                    // Se guarda el precio original sin descuento
                    BigDecimal precioOriginal = producto.getPrecio();
                    // Se guarda el precio con el descuento aplicado
                    BigDecimal precioConDescuento = calculateUnitPriceProducto(producto);

                    //Creamos los detallePedidoEntity y seteamos los atributos
                    DetallePedidoEntity newDetalle = new DetallePedidoEntity();
                    newDetalle.setCantidad(requestDetalle.getCantidad());
                    newDetalle.setPrecioUnitario(precioOriginal);
                    newDetalle.setPrecioDescuento(precioConDescuento);

                    if (currentOferta(producto)){
                        newDetalle.setDescuentoAplicado(producto.getOferta().getPorcentaje());
                    }else {
                        // Si no tiene una oferta asignada el producto el descuento se le setea a "0.0"
                        newDetalle.setDescuentoAplicado(0.0);
                    }

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
        else throw new ResourceNotFoundException("El pedido con la ID: " + id + " no existe");
    }

    public PedidoResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<PedidoResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /// ----- Delete -----

    // Cancelar Pedido
    public void deleteById(Long id){
        PedidoEntity pedido = getEntityById(id);
        //Verificamos que el pedido no este cancelado
        if(pedido.getEstadoPedido().equals(EstadoPedido.CANCELADO)) {
            throw new InactiveResourceException("El pedido con la ID: " + id + " ya fue cancelado");
        }

        if (pedido.getEstadoPedido() != EstadoPedido.PENDIENTE){
            throw new InvalidResourceStateException("Solo se pueden cancelar pedidos con estado pendiente");
        }

        if (pedido.getEstadoPago() != EstadoPago.PENDIENTE){
            throw new InvalidResourceStateException("Solo se pueden cancelar pedidos con estado de pago pendiente");
        }
        //Recorremos el detalle de productos y devolvemos el stock
        pedido.getProductos()
                .forEach(detalle -> {
                    ProductoEntity producto = productoService.getEntityById(detalle.getProducto().getId());
                    producto.setStock(producto.getStock() + detalle.getCantidad());
                    productoService.saveEntity(producto);
                });

        //Seteamos el pedido como CANCELADO y el estado del pago queda anulado porque el pedido se cancelo
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        pedido.setEstadoPago(EstadoPago.PEDIDO_CANCELADO);

        //Guardamos en el repo
        repository.save(pedido);
    }

    // Sirve para registrar una Entrega de un pedido por ID
    @Transactional
    public PedidoResponseDTO deliverPedido(Long id){
        PedidoEntity pedido = getEntityById(id);

        // Validamos que el Estado del Pedido no este CANCELADO
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO){
            throw new InvalidResourceStateException("No se puede entregar un pedido cancelado");
        }

        // Validamos que el Estado del Pedido ya no figure como ENTREGADO
        if (pedido.getEstadoPedido() == EstadoPedido.ENTREGADO){
            throw new InvalidResourceStateException("El pedido ya fue entregado");
        }

        // Se setea el Estado del pedido como ENTREGADO
        pedido.setEstadoPedido(EstadoPedido.ENTREGADO);

        return mapper.toResponse(repository.save(pedido));
    }

    /// ----- Formateo -----

    private String formatearPisoDepto(DireccionEntity direccion) {
        if (direccion.getPiso() == null && direccion.getDepartamento() == null) {
            return "Sin especificar";
        }

        if (direccion.getPiso() == null) {
            return "Depto: " + direccion.getDepartamento();
        }

        if (direccion.getDepartamento() == null || direccion.getDepartamento().isBlank()) {
            return "Piso: " + direccion.getPiso();
        }

        return "Piso: " + direccion.getPiso()
                + " | Depto: " + direccion.getDepartamento();
    }

    /// ---- Funciones Utiles ----

    // Sirve para validar que no se cargue el mismo producto 2 veces en la misma request
    private void validateNotRepeatProduct(PedidoCreateRequestDTO request) {

        long cantProductosUnicos = request.getDetalles().stream()
                .map(detalle -> detalle.getProductoId())
                .distinct()
                .count();

        if (cantProductosUnicos != request.getDetalles().size()){
            throw new DuplicateResourceException("No se puede repetir el mismo Producto en el detalle del pedido");
        }
    }

    // Se usa para saber si el producto tiene una oferta asociada y si esa oferta esta vigente actualmente
    private boolean currentOferta(ProductoEntity producto){

        // Se valida que el producto tenga alguna oferta asociada (producto.getOferta == null)
        if (producto.getOferta() == null) {
            return false;
        }

        // Valida que la fecha de Inicio y la de Fin no sean nulas
        if (producto.getOferta().getFechaInicio() == null || producto.getOferta().getFechaFin() == null){
            return false;
        }
            // Se crea una variable guardando la fecha actual para comparar con
            // las fechas de Inicio y Fin de la Oferta asociada al Producto recibido
            LocalDate today = LocalDate.now();

            //Se hace la comparacion para saber si la fecha de Hoy (today) esta
            // por fuera del rango de las fechas de vigencia de la oferta
            return !today.isBefore(producto.getOferta().getFechaInicio()) && !today.isAfter(producto.getOferta().getFechaFin());
            // Seria: "Si hoy NO esta antes de la fecha de Inicio de la oferta y Hoy no esta despues de la fecha de Fin"
    }

    // Calcula el precio unitario final de un producto
    private BigDecimal calculateUnitPriceProducto(ProductoEntity producto){

        BigDecimal precioOriginal = producto.getPrecio();

        // Se valida que el producto tenga asociada una oferta y que este vigente
        if (!currentOferta(producto)){
            return precioOriginal;
        }

        // Se convierte el porcentaje de un Double a un BigDecimal que es mas preciso
        BigDecimal porcentaje = BigDecimal.valueOf(producto.getOferta().getPorcentaje());

        // Se calcula el descuento del precio dependiendo el porcentaje de la oferta
        BigDecimal descuento = precioOriginal
                .multiply(porcentaje)
                // Cuando se divide con BigDecimal te pide
                // especificar por parametros: (por cuanto se divide (100), cuantos decimales dejo (2), como quiero redondear el resultado (HALF_UP)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return precioOriginal.subtract(descuento);
    }


    /// ----- Busqueda en BDD -----

    public List<PedidoEntity> getPedidosByDireccion(String calle, Integer numero){
        return repository.findByCalleIgnoreCaseAndNumero(calle, numero);
    }

    public List<PedidoEntity> getPedidosByFecha(LocalDate fecha){
        return repository.findByFecha(fecha);
    }

    public List<PedidoEntity> getPedidosByEstado(EstadoPedido estado){
        return repository.findByEstadoPedido(estado);
    }

    public List<PedidoEntity> getPedidosByEstadoPago(EstadoPago estado){
        return repository.findByEstadoPago(estado);
    }

    public VentasPorMesResponseDTO  getPedidosByMes(Integer anio, Integer mes){
        Optional<VentasPorMesResponseDTO> ventasPorMes = repository.getVentasPorMes(anio, mes);
        if(ventasPorMes.isEmpty())
            throw new EmptyListException("No se encontro ningun pedido entregado y pagado en ese mes");

        return ventasPorMes.get();
    }

    public VentasPorRangoResponseDTO getVentasByRango(LocalDate fechaInicio, LocalDate fechaFin){
        Optional<VentasPorRangoResponseDTO> ventasPorRango = repository.getVentasPorRango(fechaInicio, fechaFin);
        if(ventasPorRango.isEmpty())
            throw new EmptyListException("No se encontro ningun pedido entregado y pagado en ese rango de fechas");

        return ventasPorRango.get();
    }

    public List<CantidadPedidosPorEstadoResponseDTO> getCantidadPedidosPorEstadoPorMes(Integer anio, Integer mes){
        List<CantidadPedidosPorEstadoResponseDTO> cantidadPedidos = repository.getCantidadPedidosPorEstadoPorMes(anio, mes);
        if(cantidadPedidos.isEmpty())
            throw new EmptyListException("No se encontro ningun pedido en ese rango de fechas");
        return cantidadPedidos;
    }

    public TicketPromedioResponseDTO getTicketPromedioVentas(Integer anio, Integer mes){
        TicketPromedioResponseDTO ticket = repository.getTicketPromedioVentas(anio, mes);
        if(ticket.getCantidadPedidos() == 0)
            throw new NoMatchingResultsException("No hubo ninguna venta en ese mes");
        return ticket;
    }


}
