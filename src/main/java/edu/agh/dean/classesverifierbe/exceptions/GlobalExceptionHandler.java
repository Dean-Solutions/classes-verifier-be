package edu.agh.dean.classesverifierbe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserNotFoundException.class, UserTagAlreadyExistsException.class,
            UserTagNotFoundException.class, InvalidIndexException.class})
    @ResponseBody
    public ResponseEntity<?> handleCustomExceptions(Exception ex) {
        if (ex instanceof UserNotFoundException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof UserAlreadyExistsException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.CONFLICT);
        }
        else if(ex instanceof UserTagNotFoundException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof UserTagAlreadyExistsException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.CONFLICT);
        } else if (ex instanceof InvalidIndexException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SemesterNotFoundException.class, SemesterAlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<?> handleSemesterExceptions(Exception ex) {
        if (ex instanceof SemesterNotFoundException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof SemesterAlreadyExistsException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SubjectNotFoundException.class, SubjectAlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<?> handleSubjectExceptions(Exception ex) {
        if (ex instanceof SubjectNotFoundException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof SubjectAlreadyExistsException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SubjectTagNotFoundException.class, SubjectTagAlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<?> handleSubjectTagExceptions(Exception ex) {
        if (ex instanceof SubjectTagNotFoundException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof SubjectTagAlreadyExistsException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EnrollmentAlreadyExistException.class})
    @ResponseBody
    public ResponseEntity<?> handleEnrollmentExceptions(Exception ex) {
        if (ex instanceof EnrollmentAlreadyExistException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RequestNotFoundException.class})
    @ResponseBody
    public ResponseEntity<?> handleRequestExceptions(Exception ex) {
        if (ex instanceof RequestNotFoundException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RequestEnrollNotFoundException.class, RequestEnrollSingleRequestAlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<?> handleRequestEnrollException(Exception ex) {
        if (ex instanceof RequestEnrollNotFoundException) {
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof  RequestEnrollSingleRequestAlreadyExistsException){
            return new ResponseEntity<>(mapToJson(ex), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.BAD_REQUEST);
    }
    private Map<String, String> mapToJson(Exception ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return body;
    }

    @ExceptionHandler({NoPermissionException.class})
    @ResponseBody
    public ResponseEntity<?> handleNoPermissionException(Exception ex) {
        return new ResponseEntity<>(mapToJson(ex), HttpStatus.FORBIDDEN);
    }
}