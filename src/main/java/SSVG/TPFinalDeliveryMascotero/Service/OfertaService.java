package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.InvalidDateRangeException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
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

        //Validamos que la fecha de FIN de la oferta no sea antes que la de inicio
        validateDates(request.getFechaInicio(), request.getFechaFin());

        //Creamos la nueva oferta y le pasamos los datos del RequestDTO
        OfertaEntity newOferta = mapper.toEntity(request);

        //Buscamos todos los productos que hay en el request
        List<ProductoEntity> productos = (request.getProductosIds().stream().
                map(productoService::getEntityById)
                .toList());

        //Guardamos esos productos en la oferta
        newOferta.setProductos(productos);

        //Guardamos la oferta en el repo
        OfertaEntity savedOferta = repository.save(newOferta);

        //Hacemos la relacion inversa. A cada producto le asignamos la oferta
        productos.forEach(producto-> producto.setOferta(newOferta));

        //Guardamos los productos en el repo
        productos.forEach(productoService::saveEntity);

        return mapper.toResponse(savedOferta);
    }

    public OfertaEntity getEntityById(Long id) {
        Optional<OfertaEntity> entity = repository.findById(id);
        if (entity.isPresent())
            return entity.get();
            else throw new ResourceNotFoundException("La Oferta con el ID: " + id + " no existe");
    }

    public OfertaResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<OfertaResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

   // @Transactional
   // public OfertaResponseDTO updateOferta(Long id, OfertaCreateRequestDTO request) {    }

    public void deleteById(Long id) {
        OfertaEntity oferta = getEntityById(id);

        clearProductsOffer(oferta);

        repository.delete(oferta);
    }

    ///----- Validations

    private void validateDates(LocalDate inicio, LocalDate fin) {
        if (fin.isBefore(inicio)) {
            throw new InvalidDateRangeException("La fecha de fin de la oferta no puede ser antes de la fecha de inicio");
        }
    }

    // Se hace la desasociacion de una Oferta y sus productos, para utilizar
    // en los metodos de actualizacion y eliminacion de Ofertas
    private void clearProductsOffer(OfertaEntity oferta){

        // Aca se elimina la "relacion" entre una oferta y sus productos asociados,
        // dejando a los productos con la oferta en "null"
        if (oferta.getProductos() != null){
            oferta.getProductos().forEach(producto -> producto.setOferta(null));
            oferta.getProductos().clear();
        }
    }
}