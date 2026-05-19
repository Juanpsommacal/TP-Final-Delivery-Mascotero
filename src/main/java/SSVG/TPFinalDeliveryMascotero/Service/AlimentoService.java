package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//Service para operaciones especificas de Alimento (clase hija). (crearAlimento, EditarAlimento)
public class AlimentoService {

    private final AlimentoRepository repository;

}
