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

    //verificar id inexistente
    @Test
    void inexistentIdShouldThrowNotFound() {
        Long nonexistentId = 999L;

        assertThatThrownBy(() -> service.getById(nonexistentId))
                .isInstanceOf(edu.espe.springpruebaoscarchanataxi.web.advice.NotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

}
