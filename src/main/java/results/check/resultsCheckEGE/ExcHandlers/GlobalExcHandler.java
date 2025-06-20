package results.check.resultsCheckEGE.ExcHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.UnknownContentTypeException;
import results.check.resultsCheckEGE.Exceptions.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExcHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorOnClientSideExc(ErrorOnClientSide exc){
        return new ResponseEntity<>(Map.of("error_message", "Произошла неизвестная ошибка со стороны клиента"), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorOnMosRuSide(ErrorOnMosRuSide exc){
        return new ResponseEntity<>(Map.of("error_message", "Произошла неизвестная ошибка со стороны мос ру"), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorWhileGetResultOfRequestExc(ErrorWhileGetResultOfRequest exc){
        return new ResponseEntity<>(Map.of("error_message", exc.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> unknownContentTypeExc(UnknownContentTypeException exc){
        return new ResponseEntity<>(Map.of("error_message", "Ошибка при получении ответа от мос ру"), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorWhileGetUniqueHashExc(ErrorWhileGetUniqueHashExc exc){
        return new ResponseEntity<>(Map.of("error_message", exc.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorWhileGetAppIdExc(ErrorWhileGetAppIdExc exc){
        return new ResponseEntity<>(Map.of("error_message", exc.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> invalidCookieExc(InvalidCookieExc exc){
        return new ResponseEntity<>(Map.of("error_message", exc.getMessage()), INTERNAL_SERVER_ERROR);
    }
}
