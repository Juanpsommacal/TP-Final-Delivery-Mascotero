package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Repository.AntipulgasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
//Service para operaciones especificas de Antipulgas (clase hija). (crearAntipulgas, EditarAntipulgas)
public class AntipulgasService {

    private final AntipulgasRepository repository;
}
