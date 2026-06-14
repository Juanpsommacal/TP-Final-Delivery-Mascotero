package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ProductoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//Service para operaciones generales de producto. (getAll, getById, eliminarProducto)
public class ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    public List<ProductoResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
    public List<ProductoEntity> getAll2(){
        return repository.findAll().stream()
                .toList();
    }


    public ProductoEntity getEntityById(Long id){
        Optional<ProductoEntity> producto = repository.findById(id);
        if (producto.isPresent())
            return producto.get();
        else throw new ResourceNotFoundException("El producto no existe");
    }

    public void saveEntity(ProductoEntity entity){
        repository.save(entity);
    }

    public List<ProductoResponseDTO> saveAllProductsOriginalPrice(List<ProductoEntity> list) {

        return repository.saveAll(list)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}
