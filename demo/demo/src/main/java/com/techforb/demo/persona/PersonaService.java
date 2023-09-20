package com.techforb.demo.persona;

import com.techforb.demo.transferencias.Transferencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    private final List<Transferencia> transferencias = new ArrayList<>();

    @Autowired
    public PersonaService(PersonaRepository personaRepository){
        this.personaRepository = personaRepository;
    }
    public List<Persona> getPersonas(){
        return this.personaRepository.findAll();
    }

    public void newPersona(Persona persona) {
        personaRepository.save(persona);
    }

    public void borrarPersona(Long id){
        personaRepository.deleteById(id);
    }

    public Persona getPersonaPorId(Long id) {
        Optional<Persona> personaOptional = personaRepository.findById(id);
        return personaOptional.orElse(null); // Devuelve null si no se encuentra la persona.
    }

    public float ingresarDinero(Long id, float cantidad) {
        Optional<Persona> personaOptional = personaRepository.findById(id);
        if (personaOptional.isPresent()) {
            Persona persona = personaOptional.get();
            float nuevoSalario = persona.getSalary() + cantidad;
            persona.setSalary(nuevoSalario);
            personaRepository.save(persona);
            return nuevoSalario;
        }
        return -1; // Devuelve -1 si no se encuentra la persona.
    }
    public float extraerDinero(Long id, float cantidad) {
        Optional<Persona> personaOptional = personaRepository.findById(id);
        if (personaOptional.isPresent()) {
            Persona persona = personaOptional.get();
            if (persona.getSalary() >= cantidad) {
                float nuevoSalario = persona.getSalary() - cantidad;
                persona.setSalary(nuevoSalario);
                personaRepository.save(persona);
                return nuevoSalario;
            } else {
                return -2; // Devuelve -2 si no hay saldo suficiente
            }
        }
        return -1; // Devuelve -1 si no se encuentra la persona.
    }

    public boolean existePersona(Long id) {
        // Itera sobre la lista de personas y verifica si alguna tiene el ID proporcionado
        for (Persona persona : getPersonas()) {
            if (persona.getId().equals(id)) {
                return true; // La persona con el ID proporcionado existe
            }
        }
        return false; // La persona con el ID proporcionado no se encontró
    }


    public ResponseEntity<String> transferirDinero(@RequestBody Map<String, Object> requestBody) {
        Long idOrigen = parseLong(requestBody.get("idOrigen"));
        Long idDestino = parseLong(requestBody.get("idDestino"));
        Float cantidad = parseFloat(requestBody.get("cantidad"));

        if (idOrigen != null && idDestino != null && cantidad != null) {
            // Verificar que las personas de origen y destino existen
            boolean origenExiste = existePersona(idOrigen);
            boolean destinoExiste = existePersona(idDestino);

            if (!origenExiste || !destinoExiste) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Una o ambas personas no fueron encontradas.");
            }

            // Extraer dinero de la persona de origen
            float resultadoExtraccion = extraerDinero(idOrigen, cantidad);

            if (resultadoExtraccion == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente en la persona de origen.");
            }

            // Ingresar dinero en la persona de destino
            float nuevoSaldoDestino = ingresarDinero(idDestino, cantidad);

            if (nuevoSaldoDestino != -1) {
                // Registrar la transferencia en la lista
                Transferencia nuevaTransferencia = new Transferencia(idOrigen, idDestino, cantidad);
                transferencias.add(nuevaTransferencia);

                return ResponseEntity.ok("Transferencia exitosa. Nuevo saldo en la persona de origen: " + resultadoExtraccion +
                        ". Nuevo saldo en la persona de destino: " + nuevoSaldoDestino);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona de destino no encontrada.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los datos de entrada son inválidos.");
        }
    }


    // Metodos auxiliares para hacer validaciones del tipo de dato
    private Long parseLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Float parseFloat(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            try {
                return Float.parseFloat((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }


    // Método para obtener las últimas transacciones
    public List<Transferencia> obtenerTodasLasTransacciones() {
        return transferencias;
    }

}
