package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public final class DireccionConsultas {

    private DireccionConsultas() {}

    public static Specification<DireccionEntity> buscar(String busqueda) {
        return (root, query, cb) -> {
            Expression<String> texto = Consultas.unir(cb, root.get("calle"), root.get("numero").as(String.class));
            return Consultas.cadaPalabra(cb, busqueda, palabra -> Consultas.contiene(cb, texto, palabra));
        };
    }
}
