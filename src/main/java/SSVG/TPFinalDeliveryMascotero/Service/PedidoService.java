package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.*;
import SSVG.TPFinalDeliveryMascotero.Exception.*;
import SSVG.TPFinalDeliveryMascotero.Mapper.DireccionMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.PedidoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.OrdenRepartoRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.ProgramarEntregaRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.FaltanteProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.PedidosPaginaResponseDTO;
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
import SSVG.TPFinalDeliveryMascotero.Repository.Consultas.Consultas;
import SSVG.TPFinalDeliveryMascotero.Repository.Consultas.PedidoConsultas;
import SSVG.TPFinalDeliveryMascotero.Repository.Consultas.SumaConFiltros;
import SSVG.TPFinalDeliveryMascotero.Repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final SumaConFiltros sumaConFiltros;

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
        // La falta de stock ya no frena el pedido: queda esperando la mercaderia
        Map<String, List<String>> errorsMap = new HashMap<>();
        List<String> productosInactivos = new ArrayList<>();

        request.getDetalles()
                .forEach(detalle -> {

                    ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());

                    //Verificamos que no haya ningun producto inactivo
                    if(!producto.getActivo()){
                        productosInactivos.add(producto.getMarca().concat(" ").concat(producto.getNombre()));
                    }
                });

        //Si hay productos inactivos tiramos la excepcion
        if(!productosInactivos.isEmpty()){
            errorsMap.put("Productos inactivos: ", productosInactivos);
            throw new InsufficientStockException(errorsMap);
        }

        //Si alcanza el stock de todo se reserva ahora; si falta algo, no se reserva nada hasta tener el pedido completo
        boolean hayStock = request.getDetalles().stream()
                .allMatch(detalle -> productoService.getEntityById(detalle.getProductoId()).getStock() >= detalle.getCantidad());

        //Creamos el pedido vacio y vamos seteando los atributos
        PedidoEntity newPedido = new PedidoEntity();

        newPedido.setCliente(cliente);
        newPedido.setFecha(LocalDate.now());
        newPedido.setEstadoPedido(hayStock ? EstadoPedido.A_COORDINAR : EstadoPedido.FALTA_MERCADERIA);
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

        //Descontamos el stock de los productos, solo si el pedido se puede completar
        if (hayStock) {
            request.getDetalles()
                    .forEach(detalle -> {
                        ProductoEntity producto = productoService.getEntityById(detalle.getProductoId());
                        producto.setStock(producto.getStock() - detalle.getCantidad());
                        productoService.saveEntity(producto);
                    });
        }

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

    public List<PedidoResponseDTO> getByClienteId(Long clienteId){
        clienteService.getEntityById(clienteId);
        return repository.findByClienteId(clienteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PedidosPaginaResponseDTO getPagina(int page, int size, String orden, String busqueda, EstadoPedido estado,
                                              EstadoPago estadoPago, boolean conDeuda, boolean verCancelados,
                                              LocalDate desde, LocalDate hasta){
        Specification<PedidoEntity> filtro = PedidoConsultas.filtrar(busqueda, estado, estadoPago, conDeuda, verCancelados, desde, hasta);
        Specification<PedidoEntity> ordenado = Consultas.ordenadoPor(orden, PedidoConsultas.COLUMNAS, "id", false);

        return new PedidosPaginaResponseDTO(
                repository.findAll(filtro.and(ordenado), Consultas.pagina(page, size)).map(mapper::toResponse),
                sumaConFiltros.sumar(PedidoEntity.class, filtro, PedidoConsultas::saldoPositivo));
    }

    /// ----- Reparto y mercaderia -----

    public Map<EstadoPedido, Long> getContadores(){
        Map<EstadoPedido, Long> contadores = new EnumMap<>(EstadoPedido.class);
        Arrays.stream(EstadoPedido.values()).forEach(estado -> contadores.put(estado, 0L));
        repository.getCantidadPorEstado().forEach(fila -> contadores.put(fila.getEstado(), fila.getCantidad()));
        return contadores;
    }

    // Lo que hay que conseguir para completar los pedidos en espera, sumado por producto
    public List<FaltanteProductoResponseDTO> getFaltantes(){
        Map<Long, List<DetallePedidoEntity>> porProducto = repository.findByEstadoPedido(EstadoPedido.FALTA_MERCADERIA).stream()
                .flatMap(pedido -> pedido.getProductos().stream())
                .collect(Collectors.groupingBy(detalle -> detalle.getProducto().getId()));

        return porProducto.entrySet().stream()
                .map(entrada -> {
                    ProductoEntity producto = entrada.getValue().getFirst().getProducto();
                    int pedida = entrada.getValue().stream().mapToInt(DetallePedidoEntity::getCantidad).sum();
                    return new FaltanteProductoResponseDTO(entrada.getKey(), producto.getMarca() + " " + producto.getNombre(),
                            pedida, producto.getStock(), Math.max(0, pedida - producto.getStock()), entrada.getValue().size());
                })
                .filter(faltante -> faltante.faltante() > 0)
                .sorted(Comparator.comparing(FaltanteProductoResponseDTO::nombreProducto))
                .toList();
    }

    // Entregas de un dia. Si es hoy suma las atrasadas, para que no queden olvidadas
    public List<PedidoResponseDTO> getReparto(LocalDate fecha){
        LocalDate dia = fecha != null ? fecha : LocalDate.now();
        List<PedidoEntity> pedidos = new ArrayList<>(repository.findByEstadoPedidoAndFechaEntrega(EstadoPedido.PROGRAMADO, dia));
        if (dia.equals(LocalDate.now())) {
            pedidos.addAll(repository.findByEstadoPedidoAndFechaEntregaBefore(EstadoPedido.PROGRAMADO, dia));
        }

        // Primero el orden que eligio el usuario; lo que no ordeno va al final: atrasados y despues por calle
        pedidos.sort(Comparator.comparing(PedidoEntity::getOrdenReparto, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(PedidoEntity::getFechaEntrega)
                .thenComparing(PedidoEntity::getCalle, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(PedidoEntity::getNumero));

        return pedidos.stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public void guardarOrdenReparto(OrdenRepartoRequestDTO request){
        List<Long> ids = request.getPedidoIds();
        for (int posicion = 0; posicion < ids.size(); posicion++) {
            PedidoEntity pedido = getEntityById(ids.get(posicion));
            if (pedido.getEstadoPedido() != EstadoPedido.PROGRAMADO) {
                throw new InvalidResourceStateException("El pedido #" + pedido.getId() + " no esta programado para entregar");
            }
            pedido.setOrdenReparto(posicion);
        }
    }

    // Llego la mercaderia: se reserva el pedido completo y queda listo para coordinar
    @Transactional
    public PedidoResponseDTO confirmarMercaderia(Long id){
        PedidoEntity pedido = getEntityById(id);

        if (pedido.getEstadoPedido() != EstadoPedido.FALTA_MERCADERIA) {
            throw new InvalidResourceStateException("Este pedido no esta esperando mercaderia");
        }

        List<String> faltantes = pedido.getProductos().stream()
                .filter(detalle -> detalle.getProducto().getStock() < detalle.getCantidad())
                .map(detalle -> detalle.getProducto().getMarca() + " " + detalle.getProducto().getNombre()
                        + " (hay " + detalle.getProducto().getStock() + ", se necesitan " + detalle.getCantidad() + ")")
                .toList();
        if (!faltantes.isEmpty()) {
            throw new InvalidResourceStateException("Todavia no alcanza el stock de: " + String.join(", ", faltantes));
        }

        pedido.getProductos().forEach(detalle -> {
            ProductoEntity producto = detalle.getProducto();
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoService.saveEntity(producto);
        });

        pedido.setEstadoPedido(EstadoPedido.A_COORDINAR);
        return mapper.toResponse(repository.save(pedido));
    }

    // Pacta el dia de entrega. Tambien sirve para cambiarlo si ya estaba programado
    @Transactional
    public PedidoResponseDTO programarEntrega(Long id, ProgramarEntregaRequestDTO request){
        PedidoEntity pedido = getEntityById(id);

        if (pedido.getEstadoPedido() != EstadoPedido.A_COORDINAR && pedido.getEstadoPedido() != EstadoPedido.PROGRAMADO) {
            throw new InvalidResourceStateException("Solo se puede programar la entrega de un pedido con la mercaderia lista");
        }

        // En otro dia el recorrido es otro: vuelve a quedar sin ordenar
        if (!request.getFechaEntrega().equals(pedido.getFechaEntrega())) {
            pedido.setOrdenReparto(null);
        }
        pedido.setFechaEntrega(request.getFechaEntrega());
        pedido.setEstadoPedido(EstadoPedido.PROGRAMADO);
        return mapper.toResponse(repository.save(pedido));
    }

    // El cliente no puede el dia pactado y todavia no hay otro: vuelve a quedar por coordinar
    @Transactional
    public PedidoResponseDTO volverACoordinar(Long id){
        PedidoEntity pedido = getEntityById(id);

        if (pedido.getEstadoPedido() != EstadoPedido.PROGRAMADO) {
            throw new InvalidResourceStateException("Solo se puede volver a coordinar un pedido programado");
        }

        pedido.setFechaEntrega(null);
        pedido.setOrdenReparto(null);
        pedido.setEstadoPedido(EstadoPedido.A_COORDINAR);
        return mapper.toResponse(repository.save(pedido));
    }

    /// ----- Delete -----

    // Cancelar Pedido
    @Transactional
    public void deleteById(Long id){
        PedidoEntity pedido = getEntityById(id);
        //Verificamos que el pedido no este cancelado
        if(pedido.getEstadoPedido().equals(EstadoPedido.CANCELADO)) {
            throw new InactiveResourceException("El pedido con la ID: " + id + " ya fue cancelado");
        }

        if (pedido.getEstadoPedido() == EstadoPedido.ENTREGADO){
            throw new InvalidResourceStateException("No se puede cancelar un pedido que ya fue entregado");
        }

        // Si se cancelara un pedido con pagos, esa plata quedaria sin pedido
        if (!pedido.getPagos().isEmpty()){
            throw new InvalidResourceStateException("No se puede cancelar un pedido que ya tiene pagos registrados");
        }

        //Si faltaba mercaderia nunca se desconto stock, asi que no hay nada que devolver
        if (pedido.getEstadoPedido() != EstadoPedido.FALTA_MERCADERIA) {
            pedido.getProductos()
                    .forEach(detalle -> {
                        ProductoEntity producto = productoService.getEntityById(detalle.getProducto().getId());
                        producto.setStock(producto.getStock() + detalle.getCantidad());
                        productoService.saveEntity(producto);
                    });
        }

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

        // Solo se entrega lo que ya tiene un dia pactado
        if (pedido.getEstadoPedido() != EstadoPedido.PROGRAMADO){
            throw new InvalidResourceStateException("Solo se pueden entregar pedidos con fecha de entrega programada");
        }

        // Se setea el Estado del pedido como ENTREGADO
        pedido.setEstadoPedido(EstadoPedido.ENTREGADO);

        // Al entregarlo, si falta pagar algo pasa a DEUDA o DEUDA_PARCIAL
        actualizarEstadoPago(pedido);

        return mapper.toResponse(repository.save(pedido));
    }

    // Registra que se le mando al cliente un recordatorio del saldo pendiente
    @Transactional
    public PedidoResponseDTO registrarRecordatorio(Long id){
        PedidoEntity pedido = getEntityById(id);

        // Solo tiene sentido recordar un saldo que existe
        if (pedido.getEstadoPago() != EstadoPago.DEUDA && pedido.getEstadoPago() != EstadoPago.DEUDA_PARCIAL){
            throw new InvalidResourceStateException("Solo se puede registrar un recordatorio en pedidos con deuda");
        }

        pedido.setUltimoRecordatorio(LocalDateTime.now());
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

    // Unico lugar donde se decide el estado del pago. Se llama al entregar el pedido y al registrar un pago.
    // - PAGADO: se pago el total, se haya entregado o no
    // - PENDIENTE: todavia no se entrego (aunque haya pagos adelantados)
    // - DEUDA: se entrego y no se pago nada
    // - DEUDA_PARCIAL: se entrego y se pago solo una parte
    public void actualizarEstadoPago(PedidoEntity pedido) {

        // Un pedido cancelado queda con PEDIDO_CANCELADO, no se recalcula
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO) {
            return;
        }

        BigDecimal totalPagado = pedido.getPagos().stream()
                .map(pago -> pago.getMonto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPagado.compareTo(pedido.getMontoTotal()) >= 0) {
            pedido.setEstadoPago(EstadoPago.PAGADO);
        } else if (pedido.getEstadoPedido() != EstadoPedido.ENTREGADO) {
            pedido.setEstadoPago(EstadoPago.PENDIENTE);
        } else if (totalPagado.compareTo(BigDecimal.ZERO) == 0) {
            pedido.setEstadoPago(EstadoPago.DEUDA);
        } else {
            pedido.setEstadoPago(EstadoPago.DEUDA_PARCIAL);
        }
    }

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
