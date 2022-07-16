package hrms.hrms.entities.concretes;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="jobseekers")
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="jobseeker_id")
    private int id;

    @Column(name="identification_no")
    private String identificationNo;

    @Column(name="birth_year")
    private String birthYear;

    @Column(name="e_mail")
    private String email;

    @Column(name="password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id")
    private Person person;
}
