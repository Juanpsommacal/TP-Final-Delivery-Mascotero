package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.CompraCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.CompraResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DetalleCompraResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
//Le decimos a nuestro mapper que use el mapper de DetalleCompra tambien
@Mapper(componentModel = "spring",
        uses = {DetalleCompraMapper.class})
public interface CompraMapper {

    public CompraEntity toEntity (CompraCreateRequestDTO request);

    //Con mapStruct podemos acceder a los atributos de los objetos que estan en nuestra entidad
    //usando source en vez de usar un metodo default
    @Mapping(target = "nombreProveedor", source = "proveedor.nombre")
    @Mapping(target = "detalleResponseDTO", source = "productos")
    public CompraResponseDTO toResponse (CompraEntity entity);

}
