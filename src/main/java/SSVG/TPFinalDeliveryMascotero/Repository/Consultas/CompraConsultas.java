package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import SSVG.TPFinalDeliveryMascotero.Model.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DetalleCompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoCompra;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CompraConsultas {

    public static final Map<String, ExpresionColumna<CompraEntity>> COLUMNAS = Map.of(
            "id", (root, query, cb) -> root.get("id"),
            "fecha", (root, query, cb) -> root.get("fecha"),
            "proveedor", (root, query, cb) -> root.get("proveedor").get("nombre"),
            "total", (root, query, cb) -> root.get("montoTotal")
    );

    private CompraConsultas() {}

    public static Specification<CompraEntity> filtrar(String busqueda, EstadoCompra estado, boolean verCanceladas) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            // Las canceladas se ocultan salvo que se pidan o se filtre justo por ese estado
            if (!verCanceladas && estado != EstadoCompra.CANCELADA) {
                filtros.add(cb.notEqual(root.get("estadoCompra"), EstadoCompra.CANCELADA));
            }
            if (estado != null) filtros.add(cb.equal(root.get("estadoCompra"), estado));

            // Cada dato por separado: unidos sin espacios, "Distribuidora 1" y la compra 20 coincidirian con "12"
            Expression<String> proveedor = root.get("proveedor").get("nombre");
            Expression<String> numero = root.get("id").as(String.class);
            filtros.add(Consultas.cadaPalabra(cb, busqueda, palabra -> {
                // Una compra tiene varios productos: alcanza con que la palabra este en uno
                Subquery<Long> enProductos = query.subquery(Long.class);
                Root<DetalleCompraEntity> detalle = enProductos.from(DetalleCompraEntity.class);
                Expression<String> producto = Consultas.unir(cb, detalle.get("producto").get("marca"), detalle.get("producto").get("nombre"));
                enProductos.select(detalle.get("id"))
                        .where(cb.equal(detalle.get("compra"), root), Consultas.contiene(cb, producto, palabra));
                return cb.or(Consultas.contiene(cb, proveedor, palabra), Consultas.contiene(cb, numero, palabra), cb.exists(enProductos));
            }));

            return cb.and(filtros.toArray(Predicate[]::new));
        };
    }

    public static Specification<CompraEntity> pendientes() {
        return (root, query, cb) -> cb.equal(root.get("estadoCompra"), EstadoCompra.PENDIENTE);
    }
}
