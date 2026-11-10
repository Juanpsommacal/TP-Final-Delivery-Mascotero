package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Getter
public class PedidosPaginaResponseDTO extends PaginaResponseDTO<PedidoResponseDTO> {

    // Lo que falta cobrar de todos los pedidos que cumplen los filtros, no solo los de esta pagina
    private final BigDecimal saldoFiltrado;

    public PedidosPaginaResponseDTO(Page<PedidoResponseDTO> page, BigDecimal saldoFiltrado) {
        super(page);
        this.saldoFiltrado = saldoFiltrado;
    }
}
