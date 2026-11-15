package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PagoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.MetodoPago;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PagosPaginaResponseDTO extends PaginaResponseDTO<PagoResponseDTO> {

    public record TotalPorMetodo(MetodoPago metodo, BigDecimal total) {}

    // Totales de todos los pagos que cumplen los filtros, para cerrar la caja
    private final BigDecimal totalFiltrado;

    private final List<TotalPorMetodo> totalPorMetodo;

    public PagosPaginaResponseDTO(Page<PagoResponseDTO> page, BigDecimal totalFiltrado, List<TotalPorMetodo> totalPorMetodo) {
        super(page);
        this.totalFiltrado = totalFiltrado;
        this.totalPorMetodo = totalPorMetodo;
    }
}
