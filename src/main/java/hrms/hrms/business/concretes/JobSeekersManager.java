package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.JobSeekersService;
import hrms.hrms.core.dataAccess.abstracts.IdentificationNoEmailDao;
import hrms.hrms.core.utilities.excelHelper.JobSeekerListExcelHelper;
import hrms.hrms.core.utilities.pdfHelper.JobSeekerListPdfHelper;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.JobSeekersDao;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.dataAccess.abstracts.SystemPersonnelDao;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.SystemPersonnel;
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

    @Autowired
    private SystemPersonnelDao systemPersonnelDao;

    private JobSeekersDto convertEntityToDto(JobSeekers jobSeekers){
        JobSeekersDto newJobSeekersDto = new JobSeekersDto();
        newJobSeekersDto.setBirthYear(jobSeekers.getBirthYear());
        newJobSeekersDto.setEmail(jobSeekers.getEmail());
        newJobSeekersDto.setFirstName(jobSeekers.getPerson().getFirstName());
        newJobSeekersDto.setLastName(jobSeekers.getPerson().getLastName());
        newJobSeekersDto.setJobposition(jobSeekers.getSystemPersonnel().getJobposition());
        newJobSeekersDto.setPassword(jobSeekers.getPassword());
        newJobSeekersDto.setIdentificationNo(jobSeekers.getIdentificationNo());

        return newJobSeekersDto;
    }

    @Override
    public DataResult<List<JobSeekersDto>> getAllJobSeekers() {
        return new SuccessDataResult<List<JobSeekersDto>>(jobSeekersDao.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()), "Bilgiler listelendi.");
    }

    @Override
    public DataResult<List<JobSeekersDto>> getAllPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of((pageNo-1),pageSize);

        if(jobSeekersDao.findAll(pageable).getContent().size() == 0){
            return new ErrorDataResult<List<JobSeekersDto>>("Kullanıcı bulunamadı.");
        }
        else{
            return new SuccessDataResult<List<JobSeekersDto>>(jobSeekersDao.findAll(pageable).getContent()
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
            SystemPersonnel newSystemPersonnel = new SystemPersonnel();

            newSystemPersonnel.setJobposition(jobSeekersDto.getJobposition());

            newPerson.setFirstName(jobSeekersDto.getFirstName());
            newPerson.setLastName(jobSeekersDto.getLastName());

            newJobSeekers.setEmail(jobSeekersDto.getEmail());
            newJobSeekers.setBirthYear(jobSeekersDto.getBirthYear());
            newJobSeekers.setIdentificationNo(jobSeekersDto.getIdentificationNo());
            newJobSeekers.setPassword(jobSeekersDto.getPassword());

            newJobSeekers.setPerson(newPerson);
            newJobSeekers.setSystemPersonnel(newSystemPersonnel);

            systemPersonnelDao.save(newSystemPersonnel);
            personDao.save(newPerson);
            jobSeekersDao.save(newJobSeekers);

            return sendEmail(jobSeekersDto.getEmail());
        }
    }

    @Override
    public Result sendEmail(String email) {
        if (identificationNoEmailDao.existsByEmail(email)){
            return new SuccessResult(email + " e-mail adresine doğrulama için e-mail gönderildi. Kayıt gerçekleştiriliyor.");
        }

        else{
            return new ErrorResult("e-mail doğrulaması gerçekleştirilemedi.");
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
