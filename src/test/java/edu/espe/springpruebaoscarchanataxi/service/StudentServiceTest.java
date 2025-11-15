package edu.espe.springpruebaoscarchanataxi.service;

import edu.espe.springpruebaoscarchanataxi.domain.Student;
import edu.espe.springpruebaoscarchanataxi.dto.StudentRequestData;
import edu.espe.springpruebaoscarchanataxi.repository.StudentRepository;
import edu.espe.springpruebaoscarchanataxi.service.impl.StudentServiceImpl;
import edu.espe.springpruebaoscarchanataxi.web.advice.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(StudentServiceImpl.class)
public class StudentServiceTest {
    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository repository;

    @Test
    void shouldNotAllowDuplicateEmail() {
        Student existing =  new Student();
        existing.setFullName("Test User");
        existing.setEmail("test@example.com");
        existing.setBirthDate(LocalDate.of(2000, 1, 1));
        existing.setActive(true);
//Oscar Chanataxi
        repository.save(existing);

        StudentRequestData req =  new StudentRequestData();
        req.setFullName("New User Dup");
        req.setEmail("test@example.com");
        req.setBirthDate(LocalDate.of(2000, 1, 1));
        req.setActive(true);

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(ConflictException.class);

        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findAll().get(0).getEmail()).isEqualTo("test@example.com");
    }

    //Oscar Chanataxi
    @Test
    void inexistentIdShouldThrowNotFound() {
        Long nonexistentId = 999L;

        assertThatThrownBy(() -> service.getById(nonexistentId))
                .isInstanceOf(edu.espe.springpruebaoscarchanataxi.web.advice.NotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

    @Test
    void deactivateStudentShouldSetActiveFalse() {
        Student student = new Student();
        student.setFullName("Oscar Chanataxi");
        student.setEmail("ofchanataxi@espe.edu.ec");
        student.setBirthDate(LocalDate.of(1995, 5, 15));
        student.setActive(true);
        repository.save(student);

        service.deactivate(student.getId());

        Student updated = repository.findById(student.getId()).orElseThrow();
        assertThat(updated.getActive()).isFalse();
    }

    //Oscar Chanataxi
    @Test
    void getStatsShouldReturnCorrectCounts() {
        Student activeStudent1 = new Student();
        activeStudent1.setFullName("Active One");
        activeStudent1.setEmail("active1@example.com");
        activeStudent1.setBirthDate(LocalDate.of(2000, 1, 1));
        activeStudent1.setActive(true);
        repository.save(activeStudent1);

        Student activeStudent2 = new Student();
        activeStudent2.setFullName("Active Two");
        activeStudent2.setEmail("active2@example.com");
        activeStudent2.setBirthDate(LocalDate.of(2001, 2, 2));
        activeStudent2.setActive(true);
        repository.save(activeStudent2);
//Oscar Chanataxi
        Student inactiveStudent1 = new Student();
        inactiveStudent1.setFullName("Inactive One");
        inactiveStudent1.setEmail("inactive1@example.com");
        inactiveStudent1.setBirthDate(LocalDate.of(2000, 1, 1));
        inactiveStudent1.setActive(false);
        repository.save(inactiveStudent1);

        Object stats = service.getStats();

        assertThat(stats).isInstanceOf(java.util.Map.class);
        java.util.Map<?, ?> statsMap = (java.util.Map<?, ?>) stats;
        assertThat(statsMap.get("total")).isEqualTo(3L);
        assertThat(statsMap.get("active")).isEqualTo(2L);
        assertThat(statsMap.get("inactive")).isEqualTo(2L);
    }

}
