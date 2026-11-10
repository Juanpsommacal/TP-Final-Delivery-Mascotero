package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.MetodoPago;
import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PagoConsultas {

    public static final Map<String, ExpresionColumna<PagoEntity>> COLUMNAS = Map.of(
            "fecha", (root, query, cb) -> root.get("fecha"),
            "cliente", (root, query, cb) -> nombreCliente(root, cb),
            "pedido", (root, query, cb) -> root.get("pedido").get("id"),
            "monto", (root, query, cb) -> root.get("monto")
    );

    private PagoConsultas() {}

    public static Specification<PagoEntity> filtrar(String busqueda, List<MetodoPago> metodos, LocalDate desde, LocalDate hasta) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            // null = sin filtro; lista vacia = no se eligio ningun metodo, asi que no hay resultados
            if (metodos != null) {
                filtros.add(metodos.isEmpty() ? cb.disjunction() : root.get("metodoPago").in(metodos));
            }
            if (desde != null) {
                filtros.add(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("fecha"), desde.atStartOfDay()));
            }
            if (hasta != null) {
                filtros.add(cb.lessThan(root.<LocalDateTime>get("fecha"), hasta.plusDays(1).atStartOfDay()));
            }

            Expression<String> texto = Consultas.unir(cb, nombreCliente(root, cb), root.get("pedido").get("id").as(String.class));
            filtros.add(Consultas.cadaPalabra(cb, busqueda, palabra -> Consultas.contiene(cb, texto, palabra)));

            return cb.and(filtros.toArray(Predicate[]::new));
        };
    }

    public static Specification<PagoEntity> conMetodo(MetodoPago metodo) {
        return (root, query, cb) -> cb.equal(root.get("metodoPago"), metodo);
    }

    private static Expression<String> nombreCliente(Root<PagoEntity> root, CriteriaBuilder cb) {
        return Consultas.unir(cb, root.get("pedido").get("cliente").get("nombre"), root.get("pedido").get("cliente").get("apellido"));
    }
}
