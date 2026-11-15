package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ProductoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.PaginaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.Consultas.Consultas;
import SSVG.TPFinalDeliveryMascotero.Repository.Consultas.ProductoConsultas;
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
        return repository.findByActivoTrue().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PaginaResponseDTO<ProductoResponseDTO> getPagina(int page, int size, String orden, String busqueda, String tipo, Integer stockMaximo){
        return new PaginaResponseDTO<>(repository.findAll(
                ProductoConsultas.filtrar(busqueda, tipo, stockMaximo)
                        .and(Consultas.ordenadoPor(orden, ProductoConsultas.COLUMNAS, "nombre", true)),
                Consultas.pagina(page, size)).map(mapper::toResponse));
    }

    public ProductoEntity getEntityById(Long id){
        Optional<ProductoEntity> producto = repository.findById(id);
        if (producto.isPresent())
            return producto.get();
        else throw new ResourceNotFoundException("El producto con la ID: " + id + " no existe");
    }

    public void saveEntity(ProductoEntity entity){
        repository.save(entity);
    }
    public List<ProductoEntity> getAll2(){
        return repository.findAll().stream()
                .toList();
    }
}
