package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ProductAlreadyHasOfferException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.OfertaMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.OfertaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OfertaService {

    private final OfertaRepository repository;
    private final OfertaMapper mapper;
    private final ProductoService productoService;

    @Transactional
    public OfertaResponseDTO createOferta(OfertaCreateRequestDTO request) {

        validateDates(request.getFechaInicio(), request.getFechaFin());

        OfertaEntity newOferta = mapper.toEntity(request);

        if (request.getProductosIds() != null && !request.getProductosIds().isEmpty()){
            List<ProductoEntity> productos = getValidProducts(request.getProductosIds());

            associateProductsToOffer(productos, newOferta);
            newOferta.setProductos(productos);
        }

        OfertaEntity savedOferta = repository.save(newOferta);

        return mapper.toResponse(repository.save(savedOferta));
    }

    public OfertaEntity getEntityById(Long id) {
        Optional<OfertaEntity> entity = repository.findById(id);
        if (entity.isPresent())
            return entity.get();
            else throw new ResourceNotFoundException("La Oferta con el ID: " +id+ " no existe");
    }

    public OfertaResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<OfertaResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public OfertaResponseDTO updateOferta(Long id, OfertaCreateRequestDTO request) {
        OfertaEntity oferta = getEntityById(id);

        validateDates(request.getFechaInicio(), request.getFechaFin());

        oferta.setNombre(request.getNombre());
        oferta.setDescripcion(request.getDescripcion());
        oferta.setPorcentaje(request.getPorcentaje());
        oferta.setFechaInicio(request.getFechaInicio());
        oferta.setFechaFin(request.getFechaFin());

        clearProductsFromOffer(oferta);

        List<ProductoEntity> productos = getValidProducts(request.getProductosIds());
        associateProductsToOffer(productos, oferta);

        oferta.setProductos(productos);

        return mapper.toResponse(repository.save(oferta));
    }

    public void deleteById(Long id) {
        OfertaEntity oferta = getEntityById(id);

        clearProductsFromOffer(oferta);

        repository.delete(oferta);
    }

    ///----- Validations

    private void validateDates(LocalDate inicio, LocalDate fin) {

        LocalDate hoy = LocalDate.now();

        if (inicio == null || fin == null) {
            return;
        }

        if (inicio.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de inicio de la oferta no puede ser anterior a la fecha actual");
        }

        if (fin.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de fin de la oferta no puede ser anterior a la fecha actual");
        }

        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser antes de la fecha de inicio de la oferta");
        }
    }

    // Se realizan dos validaciones para utilizar en la creacion y actualizacion de ofertas
    private ProductoEntity getValidProduct(Long id) {
        ProductoEntity producto = productoService.getEntityById(id);

        if (producto.getActivo() == false){
            throw new InactiveResourceException("El producto con el ID: "+ id +" esta dado de baja");
        }
        return producto;
    }

    // Este metodo Recibe una lista SOLO de IDs y te devuelve
    // una lista con los Productos completos (La entidad)
    private List<ProductoEntity> getValidProducts(List<Long> productosIds){
        return productosIds.stream()
                .map(this::getValidProduct)
                .toList();
    }

    // En este metodo se recorren todos los productos y se les asigna/asocia la oferta
    private void associateProductsToOffer(List<ProductoEntity> productos, OfertaEntity oferta){
        productos.forEach(producto -> producto.setOferta(oferta));
    }

    //

    // Se hacer la desasociacion de una Oferta y sus productos, para utilizar
    // en los metodos de actualizacion y eliminacion de Ofertas
    private void clearProductsFromOffer(OfertaEntity oferta){

        // Aca se elimina la "relacion" entre una oferta y sus productos asociados,
        // dejando a los productos con la oferta en "null"
        if (oferta.getProductos() != null){
            oferta.getProductos().forEach(producto -> producto.setOferta(null));
            oferta.getProductos().clear();
        }
    }

    @Transactional
    public OfertaResponseDTO removeAllProductsFromOffer(Long ofertaId) {

        OfertaEntity oferta = getEntityById(ofertaId);

        clearProductsFromOffer(oferta);

        OfertaEntity ofertaActualizada = repository.save(oferta);

        return mapper.toResponse(ofertaActualizada);
    }

    @Transactional
    public OfertaResponseDTO associateProductToOffer(Long ofertaId, Long productoId) {

        OfertaEntity oferta = getEntityById(ofertaId);

        ProductoEntity producto = getValidProduct(productoId);

        if (producto.getOferta() != null) {
            throw new ProductAlreadyHasOfferException(
                    "El producto ya tiene una oferta asignada."
            );
        }

        producto.setOferta(oferta);

        if (oferta.getProductos() == null) {
            oferta.setProductos(new ArrayList<>());
        }

        if (!oferta.getProductos().contains(producto)) {
            oferta.getProductos().add(producto);
        }

        return mapper.toResponse(repository.save(oferta));
    }


    private BigDecimal aplicarDescuento(
            BigDecimal precio,
            BigDecimal porcentaje) {

        return precio.multiply(
                BigDecimal.ONE.subtract(
                        porcentaje.divide(BigDecimal.valueOf(100))
                )
        );
    }


    @Transactional
    public List<ProductoResponseDTO> applyOfferToExistingProducts(Long idOferta) {

        OfertaEntity oferta = getEntityById(idOferta);

        BigDecimal porcentaje = oferta.getPorcentaje();

        List<ProductoEntity> productosActualizados = oferta.getProductos()
                .stream()
                .peek(producto -> producto.setPrecio(
                        aplicarDescuento(
                                producto.getPrecio(),
                                porcentaje
                        )
                ))
                .toList();

        return productoService.saveAllProductsOriginalPrice(productosActualizados);
    }

}