package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyListException;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.ProductoMasVendidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Repository.DetallePedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetallePedidoService {

    private final DetallePedidoRepository repository;

    public List<ProductoMasVendidoResponseDTO> getTop5ProductosMasVendidos() {
        List<ProductoMasVendidoResponseDTO> topProductos = repository.getTopProductosMasVendidos(PageRequest.of(0, 5));
        if(topProductos.isEmpty())
            throw new EmptyListException("No se encontro ningun producto vendido");
        return topProductos;
    }
}
