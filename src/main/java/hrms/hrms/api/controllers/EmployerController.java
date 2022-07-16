package hrms.hrms.api.controllers;

import hrms.hrms.business.abstracts.EmployerService;
import hrms.hrms.core.entities.dtos.EmployerGetDto;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.ErrorDataResult;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.dtos.EmployerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @GetMapping("/getAllEmployer")
    public DataResult<List<EmployerGetDto>> getAllEmployer(){
        return employerService.getAllEmployers();
    }

    @GetMapping("/getAllPage")
    public DataResult<List<EmployerGetDto>> getAllPage(int pageNo, int pageSize) {
        return employerService.getAllPage(pageNo,pageSize);
    }

    @PostMapping(name = "/addEmployer")
    public ResponseEntity<?> addEmployer(@Valid @RequestBody EmployerDto employerDto) {
        return ResponseEntity.ok(employerService.addEmployer(employerDto));
    }

    @DeleteMapping(name = "/deleteEmployer")
    public ResponseEntity<?> deleteEmployer(@RequestParam int id){
        return ResponseEntity.ok(employerService.deleteEmployer(id));
    }

    @GetMapping("/getByWebsiteMailContains")
    public DataResult<List<Employer>> getByWebsiteMailContains(@RequestParam String webMail){
        return employerService.getByWebsiteMailContains(webMail);
    }

    @GetMapping("/getByCompanyNameContains")
    public DataResult<List<Employer>> getByCompanyNameContains(@RequestParam String companyName){
        return employerService.getByCompanyNameContains(companyName);
    }

    @GetMapping("/updateEmployerPhoneNo")
    public ResponseEntity<?> updateEmployerPhoneNo(@RequestParam int id, @RequestParam String phoneNo){
        return ResponseEntity.ok(employerService.updateEmployerPhoneNo(id,phoneNo));
    }

    @GetMapping("/updateEmployerPassword")
    public ResponseEntity<?> updateEmployerPassword(@RequestParam int id, @RequestParam String password){
        return ResponseEntity.ok(employerService.updateEmployerPassword(id,password));
    }

    @GetMapping("/exportToExcelEmployer")
    public ResponseEntity<?> exportToExcelEmployer(HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(employerService.exportToExcelEmployer(response));
    }

    @GetMapping("/exportToPdfEmployer")
    public ResponseEntity<?> exportToPdfEmployer(HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(employerService.exportToPdfEmployer(response));
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