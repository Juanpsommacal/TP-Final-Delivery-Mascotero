package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

/// Valor calculado de una fila: una columna para ordenar o un monto para sumar.
@FunctionalInterface
public interface ExpresionColumna<T> {
    Expression<?> crear(Root<T> root, AbstractQuery<?> query, CriteriaBuilder cb);
}
