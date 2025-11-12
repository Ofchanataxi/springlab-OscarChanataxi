package edu.espe.springpruebaoscarchanataxi.service;

import edu.espe.springpruebaoscarchanataxi.domain.Student;
import edu.espe.springpruebaoscarchanataxi.dto.StudentRequestData;
import edu.espe.springpruebaoscarchanataxi.repository.StudentRepository;
import edu.espe.springpruebaoscarchanataxi.web.advice.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(StudentService.class)
public class StudentServiceTest {
    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository repository;

    @Test
    void shouldNotAllowDuplicateEmail() {
        Student existing =  new Student();
        existing.setFullName("Test User");
        existing.setEmail("diplucate@example.com");
        existing.setBirthDate(LocalDate.of(2000, 1, 1));
        existing.setActive(true);

        repository.save(existing);

        StudentRequestData req =  new StudentRequestData();
        req.setFullName("New User Dup");
        req.setEmail("diplucate@example.com");
        req.setBirthDate(LocalDate.of(2000, 1, 1));
        req.setActive(true);

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(ConflictException.class);
    }
}
