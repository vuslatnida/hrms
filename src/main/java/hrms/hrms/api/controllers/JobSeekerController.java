package hrms.hrms.api.controllers;

import hrms.hrms.business.abstracts.JobSeekersService;
import hrms.hrms.core.entities.dtos.JobSeekersGetDto;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;
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
@RequestMapping("/api/jobseeker")
public class JobSeekerController {

    @Autowired
    private JobSeekersService jobSeekersService;

    @GetMapping("/getAllJobSeekers")
    public DataResult<List<JobSeekersGetDto>> getAlljobSeekers() {
        return jobSeekersService.getAllJobSeekers();
    }

    @GetMapping("/getAllPage")
    public DataResult<List<JobSeekersGetDto>> getAllPage(int pageNo, int pageSize) {
        return jobSeekersService.getAllPage(pageNo,pageSize);
    }

    @PostMapping(name = "/addJobSeekers")
    public ResponseEntity<?> addJobSeekers(@Valid @RequestBody JobSeekersDto jobSeekersDto) {
        return ResponseEntity.ok(jobSeekersService.addJobSeekers(jobSeekersDto));
    }

    @DeleteMapping(name = "/deleteJobSeeker")
    public ResponseEntity<?> deleteJobSeeker(@RequestParam int id) {
        return ResponseEntity.ok(jobSeekersService.deleteJobSeeker(id));
    }

    @GetMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestParam int id, @RequestParam  String password){
        return ResponseEntity.ok(jobSeekersService.updatePassword(id, password));
    }

    @GetMapping("/getByIdentificationNoContains")
    public DataResult<List<JobSeekers>> getByIdentificationNoContains(@RequestParam String identificationNo){
        return jobSeekersService.getByIdentificationNoContains(identificationNo);
    }

    @GetMapping("/exportToExcelJobSeekers")
    public ResponseEntity<?> exportToExcelJobSeekers(HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(jobSeekersService.exportToExcelJobSeekers(response));
    }

    @GetMapping("/exportToPdfJobSeekers")
    public ResponseEntity<?> exportToPdfJobSeekers(HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(jobSeekersService.exportToPdfJobSeekers(response));
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