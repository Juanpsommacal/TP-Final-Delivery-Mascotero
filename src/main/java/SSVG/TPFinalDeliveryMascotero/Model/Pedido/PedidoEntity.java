package SSVG.TPFinalDeliveryMascotero.Model.Pedido;

import SSVG.TPFinalDeliveryMascotero.Model.Cliente.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Direccion.Direccion;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "Pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ClienteEntity cliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false)
    private Double montoTotal;

    @Embedded
    private Direccion direccion;
}
