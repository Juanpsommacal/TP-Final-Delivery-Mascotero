package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PedidoResponseDTO {

    private Long id;

    private String nombreCliente;

    private LocalDate fecha;

    private String estadoPedido;

    private BigDecimal montoTotal;

    private String estadoPago;

    private String direccionCompleta;

    private String pisoDepto;

    private List<DetallePedidoResponseDTO> detallePedido;

    private List<PagoResponseDTO> pagos;




}
