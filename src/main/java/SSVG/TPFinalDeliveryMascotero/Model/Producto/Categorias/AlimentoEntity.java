package SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alimentos")
@PrimaryKeyJoinColumn(name = "producto_id")
@DiscriminatorValue("ALIMENTO")
public class AlimentoEntity extends ProductoEntity {

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EtapaVida etapaVida;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAnimal tipoAnimal;

}
