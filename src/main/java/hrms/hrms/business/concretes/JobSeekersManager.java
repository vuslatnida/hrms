package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.JobSeekersService;
import hrms.hrms.core.dataAccess.abstracts.IdentificationNoEmailDao;
import hrms.hrms.core.entities.dtos.JobSeekersGetDto;
import hrms.hrms.core.utilities.excelHelper.JobSeekerListExcelHelper;
import hrms.hrms.core.utilities.pdfHelper.JobSeekerListPdfHelper;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.JobSeekersDao;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.dtos.request.IdentificationNoDto;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobSeekersManager implements JobSeekersService {

    @Autowired
    private JobSeekersDao jobSeekersDao;

    @Autowired
    private IdentificationNoEmailDao identificationNoEmailDao;

    @Autowired
    private PersonDao personDao;

    private JobSeekersGetDto convertEntityToDto(JobSeekers jobSeekers){
        JobSeekersGetDto newJobSeekersGetDto = new JobSeekersGetDto();
        newJobSeekersGetDto.setId(jobSeekers.getId());
        newJobSeekersGetDto.setBirthYear(jobSeekers.getBirthYear());
        newJobSeekersGetDto.setEmail(jobSeekers.getEmail());
        newJobSeekersGetDto.setFirstName(jobSeekers.getPerson().getFirstName());
        newJobSeekersGetDto.setLastName(jobSeekers.getPerson().getLastName());
        newJobSeekersGetDto.setJobposition(jobSeekers.getPerson().getJobposition());
        newJobSeekersGetDto.setPassword(jobSeekers.getPassword());
        newJobSeekersGetDto.setIdentificationNo(jobSeekers.getIdentificationNo());

        return newJobSeekersGetDto;
    }

    @Override
    public DataResult<List<JobSeekersGetDto>> getAllJobSeekers() {
        return new SuccessDataResult<List<JobSeekersGetDto>>(jobSeekersDao.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()), "Bilgiler listelendi.");
    }

    @Override
    public DataResult<List<JobSeekersGetDto>> getAllPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of((pageNo-1),pageSize);

        if(jobSeekersDao.findAll(pageable).getContent().size() == 0){
            return new ErrorDataResult<List<JobSeekersGetDto>>("Kullanıcı bulunamadı.");
        }
        else{
            return new SuccessDataResult<List<JobSeekersGetDto>>(jobSeekersDao.findAll(pageable).getContent()
                    .stream()
                    .map(this::convertEntityToDto)
                    .collect(Collectors.toList()), "Bilgiler sayfa numarası ve sırasına göre getiriliyor.");
        }
    }

    @Override
    public Result addJobSeekers(JobSeekersDto jobSeekersDto) {
        if (identificationNoEmailDao.existsByIdentificationNo(jobSeekersDto.getIdentificationNo()) || identificationNoEmailDao.existsByEmail(jobSeekersDto.getEmail())) {
            return new ErrorResult("Kişinin e-mail veya TC kimlik numarasını tekrardan kontrol ediniz. Kişi listeye eklenemedi.");
        }

        else {
            JobSeekers newJobSeekers = new JobSeekers();
            Person newPerson = new Person();

            newPerson.setJobposition(jobSeekersDto.getJobposition());
            newPerson.setFirstName(jobSeekersDto.getFirstName());
            newPerson.setLastName(jobSeekersDto.getLastName());

            newJobSeekers.setEmail(jobSeekersDto.getEmail());
            newJobSeekers.setBirthYear(jobSeekersDto.getBirthYear());
            newJobSeekers.setIdentificationNo(jobSeekersDto.getIdentificationNo());
            newJobSeekers.setPassword(jobSeekersDto.getPassword());

            newJobSeekers.setPerson(newPerson);

            personDao.save(newPerson);
            jobSeekersDao.save(newJobSeekers);

            return new SuccessResult("Kişi listeye eklendi.");
        }
    }

    @Override
    public Result deleteJobSeeker(IdentificationNoDto identificationNoDto) {
        if(jobSeekersDao.existsByIdAndIdentificationNo(identificationNoDto.getId(), identificationNoDto.getIdentificationNo())){
            jobSeekersDao.deleteById(identificationNoDto.getId());
            return new SuccessResult("Kişi listeden silindi.");
        }

        else{
            return new ErrorResult("Kişi listeden silinemedi.");
        }
    }

    @Override
    public Result updateJobSeeker(int id, String password) {
        JobSeekers newJobSeeker = jobSeekersDao.findById(id);
        newJobSeeker.setPassword(password);
        jobSeekersDao.save(newJobSeeker);
        return new SuccessResult("Kişinin şifresi güncellendi.");
    }

    @Override
    public DataResult<List<JobSeekers>> getByIdentificationNoContains(String identificationNo) {
        if (jobSeekersDao.existsByIdentificationNoContains(identificationNo)) {
            return new SuccessDataResult<List<JobSeekers>>(jobSeekersDao.getByIdentificationNoContains(identificationNo), "Data listelendi.");
        }

        else {
            return new ErrorDataResult<List<JobSeekers>>("Kullanıcı bulunamadı.");
        }
    }

    @Override
    public Result exportToExcelJobSeekers(HttpServletResponse response) {
        try {
            String fileName = "jobSeeker-list" ;

            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName+".xlsx");

            JobSeekerListExcelHelper jobSeekerListExcelHelper = new JobSeekerListExcelHelper(jobSeekersDao.findAll());
            jobSeekerListExcelHelper.export(response);
            return new SuccessResult(getAllJobSeekers().toString());
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }

    @Override
    public Result exportToPdfJobSeekers(HttpServletResponse response) {
        try {
            String fileName = "jobSeeker-list-" ;

            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName+".pdf");

            JobSeekerListPdfHelper jobSeekerListPdfHelper = new JobSeekerListPdfHelper(jobSeekersDao.findAll());
            jobSeekerListPdfHelper.export(response);
            return new SuccessResult(getAllJobSeekers().toString());
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }
}
