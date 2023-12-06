package com.example.parqueadero;
import com.example.parqueadero.controller.VehiculoController;
import com.example.parqueadero.models.Vehiculo;
import com.example.parqueadero.models.Tarifa;
import com.example.parqueadero.service.VehiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculoControllerTest {

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private Tarifa tarifa;

    @InjectMocks
    private VehiculoController vehiculoController;

        @Test
    void testProcesarSalida() {
        // Crear un vehículo de prueba
        Vehiculo vehiculo = new Vehiculo();
        LocalDateTime horaEntrada = LocalDateTime.now().minusHours(2);
        vehiculo.setHoraEntrada(horaEntrada);

        // Configurar comportamiento de mocks
        when(tarifa.getTarifaPorHora()).thenReturn(5.0);
        when(tarifa.getTarifaPorMinuto()).thenReturn(0.1);

        // Crear un objeto Model simulado
        Model model = new ExtendedModelMap();

        // Llamar al método a probar
        String viewName = vehiculoController.procesarSalida(vehiculo, model);

        // Verificar que el método guardarVehiculo fue llamado
        verify(vehiculoService).guardarVehiculo(vehiculo);

        // Verificar que se han agregado los atributos correctos al modelo
        assert model.getAttribute("vehiculo") != null;
        assert model.getAttribute("minutosEstacionado") != null;
        assert model.getAttribute("tarifaPorMinuto") != null;
        assert model.getAttribute("tarifaPorHora") != null;
        assert model.getAttribute("tarifaTotal") != null;

        // Realizar más verificaciones según tus requisitos
        // ...

        // Asegurarse de que la vista devuelta sea la correcta
        // (puedes personalizar esto según el diseño de tu aplicación)
        assert viewName.equals("confirmacionSalida");
    }
}