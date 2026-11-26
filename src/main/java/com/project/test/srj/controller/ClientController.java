package com.project.test.srj.controller;

import com.project.test.srj.dto.ApiErrorResponse;
import com.project.test.srj.dto.ClientRequest;
import com.project.test.srj.dto.GenericResponse;
import com.project.test.srj.model.Client;
import com.project.test.srj.service.ClientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operaciones CRUD y manejo de errores para clientes en SQL Server")
public class ClientController {
    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clientservice) {
        this.clientService = clientservice;
    }

    @Operation(
            summary = "Crear un cliente",
            description = "Valida los datos del cliente y registra un nuevo registro en la tabla de clientes de SQL Server."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los campos enviados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Error de conexión o transacción con SQL Server",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Client> crear(@Valid @RequestBody ClientRequest clientRequest) {
        Client creado = clientService.create(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }


    @Operation(
            summary = "Eliminar un cliente por ID",
            description = "Elimina el registro correspondiente al ID indicado en SQL Server."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Registro eliminado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Error de conexión o transacción con SQL Server",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteClient(@Parameter(description = "ID del cliente a eliminar", example = "1", required = true)
                                                            @PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse("DELETE_SUCCESS", "Registro eliminado",
                Instant.now()));
    }
}
