package SSVG.TPFinalDeliveryMascotero.Model.Cliente;

import SSVG.TPFinalDeliveryMascotero.Model.Direccion.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Pago.PagoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Pedido.PedidoEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String telefono;

    @ManyToMany
    @JoinTable(
            name = "cliente_direccion",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "direccion_id")
    )
    private List<DireccionEntity> direcciones = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<PedidoEntity> pedidos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<PagoEntity> pagos = new ArrayList<>();
}
