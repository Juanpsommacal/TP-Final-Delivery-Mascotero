package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/// Una pagina de resultados. "pagina" arranca en 0, igual que en la request.
@Getter
@Setter
public class PaginaResponseDTO<T> {

    private List<T> contenido;

    private int pagina;

    private int tamanio;

    private long totalElementos;

    private int totalPaginas;

    public PaginaResponseDTO(Page<T> page) {
        this.contenido = page.getContent();
        this.pagina = page.getNumber();
        this.tamanio = page.getSize();
        this.totalElementos = page.getTotalElements();
        this.totalPaginas = page.getTotalPages();
    }
}
