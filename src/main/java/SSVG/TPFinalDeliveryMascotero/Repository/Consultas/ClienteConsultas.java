package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public final class ClienteConsultas {

    public static final Map<String, ExpresionColumna<ClienteEntity>> COLUMNAS = Map.of(
            "nombre", (root, query, cb) -> Consultas.unir(cb, root.get("apellido"), root.get("nombre")),
            "pedidos", ClienteConsultas::cantidadPedidos,
            "ultimo", ClienteConsultas::ultimoPedido,
            "deuda", ClienteConsultas::deuda
    );

    private ClienteConsultas() {}

    public static Specification<ClienteEntity> filtrar(String busqueda, boolean soloConDeuda) {
        return (root, query, cb) -> {
            Expression<String> texto = Consultas.unir(cb, root.get("nombre"), root.get("apellido"), root.get("telefono"));
            Predicate coincide = Consultas.cadaPalabra(cb, busqueda, palabra -> Consultas.contiene(cb, texto, palabra));
            return soloConDeuda ? cb.and(coincide, cb.greaterThan(deuda(root, query, cb), BigDecimal.ZERO)) : coincide;
        };
    }

    private static Expression<Long> cantidadPedidos(Root<ClienteEntity> root, AbstractQuery<?> query, CriteriaBuilder cb) {
        Subquery<Long> cantidad = query.subquery(Long.class);
        Root<PedidoEntity> pedido = cantidad.from(PedidoEntity.class);
        return cantidad.select(cb.count(pedido)).where(deEsteCliente(pedido, root, cb));
    }

    // Los clientes que nunca pidieron quedan en null: al final de mas nuevo a mas viejo, primeros al reves
    private static Expression<LocalDate> ultimoPedido(Root<ClienteEntity> root, AbstractQuery<?> query, CriteriaBuilder cb) {
        Subquery<LocalDate> ultimo = query.subquery(LocalDate.class);
        Root<PedidoEntity> pedido = ultimo.from(PedidoEntity.class);
        return ultimo.select(cb.greatest(pedido.<LocalDate>get("fecha"))).where(deEsteCliente(pedido, root, cb));
    }

    /// Lo que deben sus pedidos en deuda: el total de esos pedidos menos lo que ya se les pago.
    private static Expression<BigDecimal> deuda(Root<ClienteEntity> root, AbstractQuery<?> query, CriteriaBuilder cb) {
        Subquery<BigDecimal> montos = query.subquery(BigDecimal.class);
        Root<PedidoEntity> pedido = montos.from(PedidoEntity.class);
        montos.select(cb.coalesce(cb.sum(pedido.<BigDecimal>get("montoTotal")), BigDecimal.ZERO))
                .where(deEsteCliente(pedido, root, cb), conDeuda(pedido, cb));

        Subquery<BigDecimal> pagado = query.subquery(BigDecimal.class);
        Root<PagoEntity> pago = pagado.from(PagoEntity.class);
        Path<PedidoEntity> pedidoDelPago = pago.get("pedido");
        pagado.select(cb.coalesce(cb.sum(pago.<BigDecimal>get("monto")), BigDecimal.ZERO))
                .where(deEsteCliente(pedidoDelPago, root, cb), conDeuda(pedidoDelPago, cb));

        return cb.diff(montos, pagado);
    }

    private static Predicate deEsteCliente(Path<PedidoEntity> pedido, Root<ClienteEntity> cliente, CriteriaBuilder cb) {
        return cb.and(cb.equal(pedido.get("cliente"), cliente),
                cb.notEqual(pedido.get("estadoPedido"), EstadoPedido.CANCELADO));
    }

    private static Predicate conDeuda(Path<PedidoEntity> pedido, CriteriaBuilder cb) {
        return pedido.get("estadoPago").in(EstadoPago.DEUDA, EstadoPago.DEUDA_PARCIAL);
    }
}
