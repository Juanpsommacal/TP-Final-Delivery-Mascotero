package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

// Chain of Responsibility: cada eslabon valida una sola regla y le pasa la posta al siguiente.
// Si una regla falla, el eslabon lanza su excepcion y la cadena se corta.
public abstract class PedidoValidationHandler {

    private PedidoValidationHandler next;

    // Devuelve el eslabon recibido para poder encadenar: a.setNext(b).setNext(c)
    public PedidoValidationHandler setNext(PedidoValidationHandler next) {
        this.next = next;
        return next;
    }

    public final void handle(PedidoValidationContext contexto) {
        check(contexto);
        if (next != null) {
            next.handle(contexto);
        }
    }

    protected abstract void check(PedidoValidationContext contexto);
}
