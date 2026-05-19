package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//Service para operaciones generales de producto. (getAll, getById, eliminarProducto)
public class ProductoService {

    private final ProductoRepository repository;
}
