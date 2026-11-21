package SSVG.TPFinalDeliveryMascotero.Model.Builder;

import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

// Builder: arma un PedidoEntity paso a paso. build() valida los datos obligatorios
// y pone los valores por defecto (fecha del dia y estado de pago PENDIENTE).
public class PedidoBuilder {

    private ClienteEntity cliente;
    private String calle;
    private Integer numero;
    private String pisoDepto;
    private EstadoPedido estadoPedido;
    private BigDecimal montoTotal;

    public PedidoBuilder paraCliente(ClienteEntity cliente) {
        this.cliente = cliente;
        return this;
    }

    // La direccion se guarda copiada en el pedido, para que no cambie si despues se edita la del cliente
    public PedidoBuilder enDireccion(String calle, Integer numero, String pisoDepto) {
        this.calle = calle;
        this.numero = numero;
        this.pisoDepto = pisoDepto;
        return this;
    }

    public PedidoBuilder conEstado(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
        return this;
    }

    public PedidoBuilder conMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
        return this;
    }

    public PedidoEntity build() {
        if (cliente == null || calle == null || numero == null
                || estadoPedido == null || montoTotal == null)
            throw new IllegalStateException("Faltan datos obligatorios del pedido");

        PedidoEntity pedido = new PedidoEntity();
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDate.now());              // valor por defecto
        pedido.setEstadoPago(EstadoPago.PENDIENTE);    // valor por defecto
        pedido.setEstadoPedido(estadoPedido);
        pedido.setCalle(calle);
        pedido.setNumero(numero);
        pedido.setPisoDepto(pisoDepto);
        pedido.setMontoTotal(montoTotal);
        return pedido;
    }
}
