package hrms.hrms.api.controllers;

import hrms.hrms.business.abstracts.EmployerService;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.ErrorDataResult;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.dtos.EmployerDto;
import hrms.hrms.entities.concretes.dtos.response.PhoneNoDto;
import hrms.hrms.entities.concretes.dtos.response.WebMailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @GetMapping("/getAllEmployer")
    public DataResult<List<Employer>> getAllEmployer(){
        return employerService.getAllEmployers();
    }

    @PostMapping(name = "/addEmployer")
    public ResponseEntity<?> addEmployer(@Valid @RequestBody EmployerDto employerDto) {
        return ResponseEntity.ok(employerService.addEmployer(employerDto));
    }

    @DeleteMapping(name = "/deleteEmployer")
    public ResponseEntity<?> deleteEmployer(@Valid @RequestBody PhoneNoDto phoneNoDto){
        return ResponseEntity.ok(employerService.deleteEmployer(phoneNoDto));
    }

    @GetMapping(name = "/getByWebsiteMail")
    public ResponseEntity<?> getByWebsiteMail(@RequestParam WebMailDto webMailDto) {
        return ResponseEntity.ok(employerService.getByWebsiteMail(webMailDto));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions){
        Map<String, String> validationErrors = new HashMap<>();

        for(FieldError fieldError : exceptions.getBindingResult().getFieldErrors()){
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorDataResult<Object> errors = new ErrorDataResult<Object>(validationErrors, "Doğrulama hataları");
        return errors;
    }
}