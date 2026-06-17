package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Mapper.PedidoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.*;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportesService {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final DetallePedidoService detallePedidoService;


    ///----- Busquedas de pedidos

    public List<PedidoResponseDTO> getPedidosByDireccion(String calle, Integer numero){
        return pedidoService.getPedidosByDireccion(calle, numero)
                .stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    public List<PedidoResponseDTO> getPedidosByFecha(LocalDate fecha){
        return pedidoService.getPedidosByFecha(fecha)
                .stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    public List<PedidoResponseDTO> getPedidosByEstado(EstadoPedido estado){
        return pedidoService.getPedidosByEstado(estado)
                .stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    public List<PedidoResponseDTO> getPedidosByEstadoPago(EstadoPago estado){
        return pedidoService.getPedidosByEstadoPago(estado)
                .stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    /// ----- Metricas

    public VentasPorMesResponseDTO getVentasByMes(Integer anio, Integer mes){
        return pedidoService.getPedidosByMes(anio, mes);
    }

    public VentasPorRangoResponseDTO getVentasByRango(LocalDate fechaInicio, LocalDate fechaFin){
        return pedidoService.getVentasByRango(fechaInicio, fechaFin);
    }

    public List<CantidadPedidosPorEstadoResponseDTO> getCantidadPedidosPorEstadoPorMes(Integer anio, Integer mes){
        return pedidoService.getCantidadPedidosPorEstadoPorMes(anio, mes);
    }

    public TicketPromedioResponseDTO getTicketPromedioVentas(Integer anio, Integer mes){
        return pedidoService.getTicketPromedioVentas(anio, mes);
    }

    public List<ProductoMasVendidoResponseDTO> getTop5ProductosMasVendidos(){
        return detallePedidoService.getTop5ProductosMasVendidos();
    }



}
