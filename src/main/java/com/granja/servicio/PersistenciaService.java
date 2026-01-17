package com.granja.servicio;

import com.granja.entidad.*;
import com.granja.modelo.*;
import com.granja.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // Marca la clase como un servicio de Spring. Se dasarrollo con java y el framework de SpringBoot
@Transactional // Gestión de transacciones a nivel de servicio. Cuando ocurre un error no deja la data se guarde
@RequiredArgsConstructor // Inyección de dependencias a través del constructor.
public class PersistenciaService {

    private final UsuarioRepositorio usuarioRepositorio;

    private final ParcelaRepositorio parcelaRepositorio;

    private final AspersorRepositorio aspersorRepositorio;

    private final SensorHumedadRepositorio sensorRepository;

    private final CultivoRepositorio cultivoRepositorio;

    private final LecturaHumedadRepositorio lecturaRepository;

    private final HistorialEncendidoRepositorio historialRepository;

    public void guardarUsuario(Usuario usuario) {
        UsuarioEntidad entity = new UsuarioEntidad(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRol()
        );
        entity.setFechaRegistro(usuario.getFechaRegistro());
        entity.setActivo(usuario.isActivo());
        usuarioRepositorio.save(entity);
    }

    public List<Usuario> cargarUsuarios() {
        List<UsuarioEntidad> entities = usuarioRepositorio.findAll();
        List<Usuario> usuarios = new ArrayList<>();
        for (UsuarioEntidad entity : entities) {
            Usuario usuario = new Usuario(
                    entity.getId(),
                    entity.getNombre(),
                    entity.getApellido(),
                    entity.getEmail(),
                    entity.getTelefono(),
                    entity.getRol()
            );
            usuario.setFechaRegistro(entity.getFechaRegistro());
            usuario.setActivo(entity.isActivo());
            usuarios.add(usuario);
        }
        return usuarios;
    }

    public void guardarParcela(Parcela parcela, Usuario usuario) {
        ParcelaEntity entity = new ParcelaEntity(parcela.getId(), parcela.getMetrosCuadrados());
        entity.setFechaCreacion(parcela.getFechaCreacion());

        if (usuario != null) {
            Optional<UsuarioEntidad> usuarioEntity = usuarioRepositorio.findById(usuario.getId());
            usuarioEntity.ifPresent(entity::setUsuarioCreador);
        }

        if (parcela.getCultivo() != null) {
            Optional<CultivoEntidad> cultivoEntity = cultivoRepositorio.findByNombre(parcela.getCultivo().getNombre());
            cultivoEntity.ifPresent(entity::setCultivo);
        }

        parcelaRepositorio.save(entity);
    }

    public void eliminarParcela(String idParcela) {
        parcelaRepositorio.deleteById(idParcela);
    }

    public void guardarAspersor(Aspersor aspersor) {
        AspersorEntidad entity = new AspersorEntidad(aspersor.getId());
        entity.setConectado(aspersor.isConectado());
        entity.setEncendido(aspersor.isEncendido());

        if (aspersor.getParcela() != null) {
            Optional<ParcelaEntity> parcelaEntity = parcelaRepositorio.findById(aspersor.getParcela().getId());
            parcelaEntity.ifPresent(entity::setParcela);
        }

        aspersorRepositorio.save(entity);
    }

    public void eliminarAspersor(String idAspersor) {
        aspersorRepositorio.deleteById(idAspersor);
    }

    public void guardarSensor(SensorHumedad sensor) {
        SensorHumedadEntidad entity = new SensorHumedadEntidad(sensor.getId());
        entity.setConectado(sensor.isConectado());
        entity.setHumedadActual(sensor.getHumedadActual());

        if (sensor.getParcela() != null) {
            Optional<ParcelaEntity> parcelaEntity = parcelaRepositorio.findById(sensor.getParcela().getId());
            parcelaEntity.ifPresent(entity::setParcela);
        }

        sensorRepository.save(entity);
    }

    public void eliminarSensor(String idSensor) {
        sensorRepository.deleteById(idSensor);
    }

    public void guardarLecturaHumedad(String sensorId, LecturaHumedad lectura) {
        LecturaHumedadEntidad entity = new LecturaHumedadEntidad(
                sensorId,
                lectura.getFecha(),
                lectura.getPorcentajeHumedad()
        );
        lecturaRepository.save(entity);
    }

    public void guardarHistorialEncendido(String aspersorId, java.time.LocalDateTime fecha) {
        HistorialEncendidoEntidad entity = new HistorialEncendidoEntidad(aspersorId, fecha);
        historialRepository.save(entity);
    }

    public void inicializarCultivos() {
        if (cultivoRepositorio.count() == 0) {
            cultivoRepositorio.save(new CultivoEntidad("Tomate", 50, 70, 72));
            cultivoRepositorio.save(new CultivoEntidad("Lechuga", 40, 60, 48));
            cultivoRepositorio.save(new CultivoEntidad("Maíz", 45, 65, 96));
            cultivoRepositorio.save(new CultivoEntidad("Zanahoria", 35, 55, 120));
            cultivoRepositorio.save(new CultivoEntidad("Fresa", 55, 75, 60));
            cultivoRepositorio.save(new CultivoEntidad("Pepino", 50, 70, 72));
            cultivoRepositorio.save(new CultivoEntidad("Cebolla", 30, 50, 144));
        }
    }

    public void actualizarCultivoParcela(String idParcela, String nombreCultivo) {
        Optional<ParcelaEntity> parcelaOpt = parcelaRepositorio.findById(idParcela);
        Optional<CultivoEntidad> cultivoOpt = cultivoRepositorio.findByNombre(nombreCultivo);

        if (parcelaOpt.isPresent() && cultivoOpt.isPresent()) {
            ParcelaEntity parcela = parcelaOpt.get();
            parcela.setCultivo(cultivoOpt.get());
            parcelaRepositorio.save(parcela);
        }
    }
}