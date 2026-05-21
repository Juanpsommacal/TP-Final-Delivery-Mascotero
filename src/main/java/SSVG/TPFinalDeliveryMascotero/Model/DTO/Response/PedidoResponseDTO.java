package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
public class PedidoResponseDTO {

    private String Cliente;

    private LocalDate fecha;

    private String estadoPedido;

    private BigDecimal montoTotal;

    private String estadoPago;

    private String direccion;

    private List<DetallePedidoResponseDTO> detallePedido;

    private List<PagoResponseDTO> pagos;




}
