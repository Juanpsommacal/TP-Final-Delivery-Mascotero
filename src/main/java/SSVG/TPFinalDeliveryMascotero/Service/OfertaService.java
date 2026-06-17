package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.*;
import SSVG.TPFinalDeliveryMascotero.Mapper.OfertaMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.OfertaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OfertaService {

    private final OfertaRepository repository;
    private final OfertaMapper mapper;
    private final ProductoService productoService;

    @Transactional
    public OfertaResponseDTO createOferta(OfertaCreateRequestDTO request) {

        // Se valida que no se pasen Productos repetidos en la request
        validateNotRepeatProduct(request);

        //Validamos que la fecha de FIN de la oferta no sea antes que la de inicio
        validateDates(request.getFechaInicio(), request.getFechaFin());

        //Creamos la nueva oferta y le pasamos los datos del RequestDTO
        OfertaEntity newOferta = mapper.toEntity(request);

        if (request.getProductosIds() != null && !request.getProductosIds().isEmpty()) {

            List<ProductoEntity> productos = getValidProducts(request.getProductosIds());

            for (ProductoEntity producto : productos) {
                if (producto.getOferta() != null) {
                    throw new ProductAlreadyHasOfferException(
                            "El producto con ID " + producto.getId() + " ya tiene una oferta asociada."
                    );
                }
            }
            // Se hace la asociacion de los productos con la oferta creada
            associateProductsToOffer(productos, newOferta);
        }

        //Guardamos la oferta en el repo
        OfertaEntity savedOferta = repository.save(newOferta);

        return mapper.toResponse(savedOferta);
    }

    public List<OfertaResponseDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public OfertaEntity getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("La Oferta con el ID: " + id + " no existe")
                );
    }

    public OfertaResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<OfertaResponseDTO> getAllActive() {

        LocalDate hoy = LocalDate.now();

        List<OfertaResponseDTO> ofertasActivas = repository.findAll()
                .stream()
                .filter(oferta ->
                        !hoy.isBefore(oferta.getFechaInicio()) &&
                                !hoy.isAfter(oferta.getFechaFin())
                )
                .map(mapper::toResponse)
                .toList();

        if (ofertasActivas.isEmpty()) {
            throw new EmptyListException("No existen ofertas activas.");
        }

        return ofertasActivas;
    }

    @Transactional
    public OfertaResponseDTO updateOferta(Long id, OfertaCreateRequestDTO request) {

        // Vaidacion para que no se pasen productos repetidos en la request
        validateNotRepeatProduct(request);
        validateDates(request.getFechaInicio(), request.getFechaFin());

        OfertaEntity oferta = getEntityById(id);

        oferta.setNombre(request.getNombre());
        oferta.setDescripcion(request.getDescripcion());
        oferta.setPorcentaje(request.getPorcentaje());
        oferta.setFechaInicio(request.getFechaInicio());
        oferta.setFechaFin(request.getFechaFin());

        List<ProductoEntity> productos =
                getValidProducts(request.getProductosIds());

        // Verifica que los productos no pertenezcan a otra oferta
        for (ProductoEntity producto : productos) {

            if (producto.getOferta() != null && !producto.getOferta().getId().equals(oferta.getId())) {

                throw new ProductAlreadyHasOfferException(
                        "El producto con ID " + producto.getId() + " ya pertenece a otra oferta."
                );
            }
        }

        clearProductsOffer(oferta);

        associateProductsToOffer(productos, oferta);

        return mapper.toResponse(repository.save(oferta));
    }

    // Elimina la oferta completamente, y antes se le extraer la oferta de los productos
    @Transactional
    public void deleteById(Long id) {
        OfertaEntity oferta = getEntityById(id);

        clearProductsOffer(oferta);

        repository.delete(oferta);
    }

    ///----- Asociacion de Productos

    @Transactional
    public OfertaResponseDTO associatedProductToOffer(Long ofertaId, Long productoId) {

        OfertaEntity oferta = getEntityById(ofertaId);

        ProductoEntity producto = getValidProduct(productoId);

        // Valido si la oferta del producto es "null"
        if (producto.getOferta() != null) {
            throw new ProductAlreadyHasOfferException("El producto ya tiene una oferta asignada.");
        }

        linkProductToOffer(producto, oferta);

        productoService.saveEntity(producto);

        return mapper.toResponse(repository.save(oferta));
    }

    // En este metodo se recorren todos los productos y se les asigna/asocia la oferta
    private void associateProductsToOffer(List<ProductoEntity> productos, OfertaEntity oferta) {
        productos.forEach(producto -> linkProductToOffer(producto, oferta));
    }


    private void linkProductToOffer(ProductoEntity producto, OfertaEntity oferta) {

        if (oferta.getProductos() == null) {
            oferta.setProductos(new ArrayList<>());
        }

        producto.setOferta(oferta);

        if (!oferta.getProductos().contains(producto)) {
            oferta.getProductos().add(producto);
        }
    }


    ///----- Desasociar Producto de Oferta

    // Le saca SOLO un producto especifico asociado a la oferta
    @Transactional
    public OfertaResponseDTO removeProductFromOffer(Long ofertaId, Long productoId) {

        // Busca la oferta y valida que exista
        OfertaEntity oferta = getEntityById(ofertaId);

        // Busca el producto y valida que exista
        ProductoEntity producto = productoService.getEntityById(productoId);

        if (producto.getOferta() == null) {
            throw new ResourceNotAssociatedException("El producto no tiene ninguna oferta asociada.");
        }

        if (!producto.getOferta().getId().equals(ofertaId)) {
            throw new ResourceNotAssociatedException("El producto no pertenece a esta oferta.");
        }

        // Se le borra la asociacion de la oferta al producto, seteandole "null"
        producto.setOferta(null);

        if (oferta.getProductos() != null) {
            oferta.getProductos().remove(producto);
        }

        productoService.saveEntity(producto);

        return mapper.toResponse(repository.save(oferta));
    }

    ///----- Validations

    // Valida las fechas de inicio y fin de la oferta
    private void validateDates(LocalDate inicio, LocalDate fin) {

        LocalDate hoy = LocalDate.now();

        // Validar que no se reciban "null" en ninguna de las fechas
        if (inicio == null || fin == null) {
            return;
        }

        // Valida que la fecha de inicio de la oferta no sea para antes de la Fecha Actual (HOY)
        if (inicio.isBefore(hoy)) {
            throw new InvalidDateRangeException("La fecha de inicio de la oferta no puede ser anterior a la fecha actual");
        }

        // Valida que la fecha de Fin de la oferta no este para una fecha anterior a la actual
        if (fin.isBefore(hoy)) {
            throw new InvalidDateRangeException("La fecha de fin de la oferta no puede ser anterior a la fecha actual");
        }

        // Valida que la fecha de Fin de la oferta este planteada para antes de la Fecha de Inicio de la misma
        if (fin.isBefore(inicio)) {
            throw new InvalidDateRangeException("La fecha de fin de la oferta no puede ser antes de la fecha de inicio");
        }
    }

    // Se busca un producto y se valida para saber si esta activo
    private ProductoEntity getValidProduct(Long id) {
        ProductoEntity producto = productoService.getEntityById(id);

        if (producto.getActivo() == false) {
            throw new InactiveResourceException("El producto con el ID: " + id + " esta dado de baja");
        }
        return producto;
    }

    // Este metodo Recibe una lista SOLO de IDs y te devuelve
    // una lista con los Productos completos (La entidad)
    private List<ProductoEntity> getValidProducts(List<Long> productosIds) {
        return productosIds.stream()
                .map(this::getValidProduct)
                .toList();
    }

    private void validateNotRepeatProduct(OfertaCreateRequestDTO request) {

        // Se obtienen la cantidad de Productos Unicos de la request eliminando los repetidos
        // para despues comparar con los que hay realmente en la request
        long cantProductosUnicos = request.getProductosIds().stream()
                .distinct()
                .count();

        // Si "cantProductosUnicos" = 2 y en la request original (sin eliminar los repetidos) habian 3 productoId
        // tira excepcion que se repitieron los productos en la request
        if (cantProductosUnicos != request.getProductosIds().size()){
            throw new DuplicateResourceException("No se puede repetir el mismo Producto en una oferta");
        }
    }

    ///---- Funciones Utiles

    // Se hace la desasociacion de una Oferta y sus productos, para utilizar
    // en los metodos de actualizacion y eliminacion de Ofertas
    private void clearProductsOffer(OfertaEntity oferta){

        // Aca se elimina la "relacion" entre una oferta y sus productos asociados,
        // dejando a los productos con la oferta en "null"
        if (oferta.getProductos() != null) {
            oferta.getProductos().forEach(producto -> producto.setOferta(null));
            oferta.getProductos().clear();
        }
    }


}