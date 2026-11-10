package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.CompraResponseDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Getter
public class ComprasPaginaResponseDTO extends PaginaResponseDTO<CompraResponseDTO> {

    // Lo que falta recibir de todas las compras que cumplen los filtros
    private final BigDecimal totalPendiente;

    public ComprasPaginaResponseDTO(Page<CompraResponseDTO> page, BigDecimal totalPendiente) {
        super(page);
        this.totalPendiente = totalPendiente;
    }
}
