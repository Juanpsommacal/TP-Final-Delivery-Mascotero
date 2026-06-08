package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ProductoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.OfertaRepository;
import SSVG.TPFinalDeliveryMascotero.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;
    private final OfertaRepository ofertaRepository;

    public List<ProductoResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ProductoEntity getEntityById(Long id){
        Optional<ProductoEntity> producto = repository.findById(id);
        if (producto.isPresent())
            return producto.get();
        else throw new ResourceNotFoundException("El producto no existe");
    }


    public ProductoResponseDTO asignarOferta(Long productoId, Long ofertaId) {

        ProductoEntity producto = repository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

        OfertaEntity oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("La oferta no existe"));

        producto.setOferta(oferta);

        ProductoEntity saved = repository.save(producto);

        return mapper.toResponse(saved);
    }
}