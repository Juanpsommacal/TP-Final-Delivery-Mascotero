package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.InvalidResourceStateException;
import SSVG.TPFinalDeliveryMascotero.Exception.LimitExceededException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.PagoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago.PagoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PagoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.MetodoPago;
import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PagoService {

    private final PagoRepository repository;
    private final PagoMapper mapper;
    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    @Transactional
    public PagoResponseDTO createPago(PagoCreateRequestDTO request){
        // Valido que el pedido exista
        PedidoEntity pedido = pedidoService.getEntityById(request.getPedidoId());

        // Se valida el Estado del Pedido, para saber si ya esta PAGADO o CANCELADO
        validateOrderStatus(pedido);

        // Calculo cuanto se pago hasta momento
        BigDecimal totalPagado = pedido.getPagos().stream()
                .map(PagoEntity::getMonto)
        // se usa ".reduce" para acumular el total
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Se calcula el saldo restante del pedido, lo que le queda por pagar al Cliente
        BigDecimal saldoRestante = pedido.getMontoTotal().subtract(totalPagado);

        // Valido que el pago que se esta recibiendo no sea mayor al saldo restante
        if (request.getMonto().compareTo(saldoRestante) > 0){
            throw new LimitExceededException("El monto ingresado ($" +request.getMonto()+
                    ") se excede de la deuda pendiente del pedido ($"+saldoRestante+").");
        }

        // Creo el Pago y le cargo todos los datos correspondientes
        PagoEntity newPago = new PagoEntity();

        newPago.setPedido(pedido);
        newPago.setMonto(request.getMonto());
        newPago.setFecha(LocalDateTime.now());
        newPago.setMetodoPago(MetodoPago.valueOf(request.getMetodoPago().trim().toUpperCase()));

        PagoEntity savedPago = repository.save(newPago);

        // Le agrego al Pedido el registro del Pago
        pedido.getPagos().add(savedPago);

        // Defino y actualizo el Estado del Pago (DEUDA_PARCIAL o PAGADO)
        BigDecimal actualTotalPagado = totalPagado.add(request.getMonto());
        if (actualTotalPagado.compareTo(pedido.getMontoTotal()) >= 0){
            pedido.setEstadoPago(EstadoPago.PAGADO);
        } else {
            pedido.setEstadoPago(EstadoPago.DEUDA_PARCIAL);
        }

        return mapper.toResponse(savedPago);
    }

    public PagoEntity getEntityById(Long id){
        Optional<PagoEntity> entity = repository.findById(id);
        if (entity.isPresent())
            return entity.get();
            else throw new ResourceNotFoundException("El pago solicitado no existe");
    }

    public PagoResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<PagoResponseDTO> listPagosByClienteId(Long clienteId){
        clienteService.getEntityById(clienteId);
        return repository.findByPedido_Cliente_Id(clienteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    ///----- Validations

    private void validateOrderStatus(PedidoEntity pedido){

        if (pedido.getEstadoPago() == EstadoPago.PAGADO){
            throw new InvalidResourceStateException("El pago no se puede registrar porque el pedido ya se encuentra totalmente pagado");
        }

        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO){
            throw new InvalidResourceStateException("No se puede registrar el pago, el pedido esta cancelado");
        }
    }

}
