package com.techforb.demo.persona;

import com.techforb.demo.transferencias.Transferencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/personas")
@CrossOrigin(origins = "http://localhost:4200/")
public class PersonaController {
    private final PersonaService personaService;

    @Autowired
    public PersonaController(PersonaService personaService){
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> getPersonas(){
        return this.personaService.getPersonas();
    }

    @PostMapping
    public void registrarPersona(@RequestBody Persona persona){
        this.personaService.newPersona(persona);
    }

    @PutMapping
    public void actualizarPersona(@RequestBody Persona persona){
        this.personaService.newPersona(persona);
    }

    @DeleteMapping(path = "{personaId}")
    public void eliminarPersona(@PathVariable("personaId") Long id){
        this.personaService.borrarPersona(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Persona> getPersonaPorId(@PathVariable Long id){
        Persona persona = personaService.getPersonaPorId(id);
        return ResponseEntity.ok(persona);
    }

    // Cambiar este metodo para hacer transferencias
    @PutMapping("{id}")
    public ResponseEntity<Persona> actualizarPersonaPorId(@PathVariable Long id,@RequestBody Persona detallesPersona){
        Persona persona = personaService.getPersonaPorId(id);

        persona.setSalary(detallesPersona.getSalary());

        //Actualizar salario
        //Persona personaActualizada = personaService.save(persona);

        return ResponseEntity.ok(persona);
    }

    // Endpoint para ingresar dinero a una persona por su ID
    @PostMapping("/{id}/ingresar-dinero")
    public ResponseEntity<Map<String, Object>> ingresarDinero(@PathVariable Long id, @RequestBody Map<String, Float> requestBody) {
        float cantidad = requestBody.get("cantidad");
        float nuevoSalario = personaService.ingresarDinero(id, cantidad);

        if (nuevoSalario != -1) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Dinero ingresado con éxito");
            response.put("nuevoSalario", nuevoSalario);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Persona no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/{id}/extraer-dinero")
    public ResponseEntity<Map<String, Object>> extraerDinero(@PathVariable Long id, @RequestBody Map<String, Float> requestBody) {
        float cantidad = requestBody.get("cantidad");
        float resultado = personaService.extraerDinero(id, cantidad);

        if (resultado == -1) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Persona no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else if (resultado == -2) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Saldo insuficiente");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Dinero retirado con éxito");
            response.put("nuevoSalario", resultado);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/transferir-dinero")
    public ResponseEntity<String> transferirDinero(@RequestBody Map<String, Object> requestBody) {
        ResponseEntity<String> response = personaService.transferirDinero(requestBody);
        return response;
    }

    @GetMapping("/ultimas-transacciones")
    public ResponseEntity<List<Transferencia>> obtenerUltimasTransacciones() {
        List<Transferencia> ultimasTransacciones = personaService.obtenerTodasLasTransacciones();
        return ResponseEntity.ok(ultimasTransacciones);
    }


}
