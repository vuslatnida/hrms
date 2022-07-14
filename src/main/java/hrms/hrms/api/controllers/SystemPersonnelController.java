package hrms.hrms.api.controllers;

import hrms.hrms.business.abstracts.SystemPersonnelService;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.ErrorDataResult;
import hrms.hrms.entities.concretes.SystemPersonnel;
import hrms.hrms.entities.concretes.dtos.SystemPersonnelDto;
import hrms.hrms.entities.concretes.dtos.request.PositionDto;
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
@RequestMapping("/api/systempersonnel")
public class SystemPersonnelController {

    @Autowired
    private SystemPersonnelService systemPersonnelService;

    @GetMapping("/getAllSystemPersonnel")
    public DataResult<List<SystemPersonnel>> getAllSystemPersonnel(){
        return systemPersonnelService.getAllSystemPersonnel();
    }

    @PostMapping(name = "/addSystemPersonnel")
    public ResponseEntity<?> addSystemPersonnel(@Valid @RequestBody SystemPersonnelDto systemPersonnelDto) {
        return ResponseEntity.ok(systemPersonnelService.addSystemPersonnel(systemPersonnelDto));
    }

    @DeleteMapping(name = "/deleteSystemPersonnel")
    public ResponseEntity<?> deleteJobSeeker(@RequestParam int id, @RequestParam PositionDto positionDto){
        return ResponseEntity.ok(systemPersonnelService.deleteSystemPersonnel(id, positionDto));
    }

    @GetMapping("/getByProductNameContains")
    public DataResult<List<SystemPersonnel>> getByJobpositionContains(@RequestParam String jobPosition){
        return systemPersonnelService.getByJobpositionContains(jobPosition);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions){
        Map<String, String> validationErrors = new HashMap<String, String>();

        for(FieldError fieldError : exceptions.getBindingResult().getFieldErrors()){
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorDataResult<Object> errors = new ErrorDataResult<Object>(validationErrors, "Doğrulama hataları");
        return errors;
    }
}
