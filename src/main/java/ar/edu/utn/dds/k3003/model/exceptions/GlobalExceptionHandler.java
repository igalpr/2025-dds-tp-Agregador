package ar.edu.utn.dds.k3003.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import ar.edu.utn.dds.k3003.model.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Recurso no encontrado", "El recurso solicitado no existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    //Por el momento solo se va a generar una respuesta de error generica para el 404 porque me molesta que se muestre tanta info
}
