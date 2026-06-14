package SSVG.TPFinalDeliveryMascotero.Mapper.Helper;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.*;
import org.springframework.stereotype.Component;

@Component
public class EnumMapper {

    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }

    public TipoAnimal toTipoAnimal(String value) {
        return value == null ? null : TipoAnimal.valueOf(normalize(value));
    }

    public EtapaVida toEtapaVida(String value) {
        return value == null ? null : EtapaVida.valueOf(normalize(value));
    }

    public UnidadMedida toUnidadMedida(String value) {
        return value == null ? null : UnidadMedida.valueOf(normalize(value));
    }

    public TipoAntipulgas toTipoAntipulgas(String value) {
        return value == null ? null : TipoAntipulgas.valueOf(normalize(value));
    }

    public EstadoPedido toEstadoPedido(String value) {
        return value == null ? null : EstadoPedido.valueOf(normalize(value));
    }

    public EstadoPago toEstadoPago(String value) {
        return value == null ? null : EstadoPago.valueOf(normalize(value));
    }

    public MetodoPago toMetodoPago(String value) {
        return value == null ? null : MetodoPago.valueOf(normalize(value));
    }
}
