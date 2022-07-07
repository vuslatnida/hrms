package hrms.hrms.api.controllers;

import hrms.hrms.business.abstracts.JobSeekersService;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoDto;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoEmailDto;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;
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
@RequestMapping("/api/jobseeker")
public class JobSeekerController {

    @Autowired
    private JobSeekersService jobSeekersService;

    @GetMapping("/getAllJobSeekers")
    public DataResult<List<JobSeekers>> getAlljobSeekers() {
        return jobSeekersService.getAllJobSeekers();
    }

    @PostMapping(name = "/addJobSeekers")
    public ResponseEntity<?> addJobSeekers(@Valid @RequestBody JobSeekersDto jobSeekersDto) {
        return ResponseEntity.ok(jobSeekersService.addJobSeekers(jobSeekersDto));
    }

    @PutMapping(name = "/getByIdentificationNoAndEmail")
    public ResponseEntity<?> getByIdentificationNoAndEmail(@Valid @RequestBody IdentificationNoEmailDto identificationNoEmailDto) {
        return ResponseEntity.ok(jobSeekersService.getByIdentificationNoAndEmail(identificationNoEmailDto));
    }

    @DeleteMapping(name = "/deleteJobSeeker")
    public ResponseEntity<?> deleteJobSeeker(@Valid @RequestBody IdentificationNoDto identificationNoDto) {
        return ResponseEntity.ok(jobSeekersService.deleteJobSeeker(identificationNoDto));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorDataResult<Object> errors = new ErrorDataResult<Object>(validationErrors, "Doğrulama hataları");
        return errors;
    }
}