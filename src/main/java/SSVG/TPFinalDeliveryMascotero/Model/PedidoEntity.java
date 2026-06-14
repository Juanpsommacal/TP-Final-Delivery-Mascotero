package SSVG.TPFinalDeliveryMascotero.Model;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estadoPedido;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Column(nullable = false, length = 20)
    private EstadoPago estadoPago;

    @Column(nullable = false)
    private String direccionCompleta;

    private String pisoDepto;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedidoEntity> productos = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PagoEntity> pagos = new ArrayList<>();

}
