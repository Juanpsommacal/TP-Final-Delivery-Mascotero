package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Mapper.AlimentoMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.AntipulgasMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.ProductoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
