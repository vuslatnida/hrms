package hrms.hrms.api.controllers;

import hrms.hrms.business.abstracts.PersonService;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.ErrorDataResult;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.dtos.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/getAllPersons")
    public DataResult<List<PersonDto>> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/getAllPage")
    public DataResult<List<PersonDto>> getAllPage(int pageNo, int pageSize) {
        return personService.getAllPage(pageNo,pageSize);
    }

    @GetMapping("/getByProductNameContains")
    public DataResult<List<Person>> getByFirstNameContains(@RequestParam String firstName){
        return personService.getByFirstNameContains(firstName);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions) {
        Map<String, String> validationErrors = new HashMap<String, String>();

        for (FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorDataResult<Object> errors = new ErrorDataResult<Object>(validationErrors, "Doğrulama hataları");
        return errors;
    }
}
