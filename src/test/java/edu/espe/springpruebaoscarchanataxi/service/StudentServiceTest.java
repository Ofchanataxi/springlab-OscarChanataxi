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

/**
 * Pruebas unitarias para StudentService
 * @author Oscar Chanataxi
 */
@DataJpaTest
@Import(StudentServiceImpl.class)
public class StudentServiceTest {

    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository repository;

    /**
     * Verifica que no se permitan emails duplicados
     * Oscar Chanataxi
     */
    @Test
    void shouldNotAllowDuplicateEmail() {
        // Arrange - Crear estudiante existente
        Student existing = new Student();
        existing.setFullName("Test User");
        existing.setEmail("test@example.com");
        existing.setBirthDate(LocalDate.of(2000, 1, 1));
        existing.setActive(true);
        repository.save(existing);

        // Act - Intentar crear otro estudiante con el mismo email
        StudentRequestData req = new StudentRequestData();
        req.setFullName("New User Dup");
        req.setEmail("test@example.com"); // Email duplicado
        req.setBirthDate(LocalDate.of(2000, 1, 1));
        req.setActive(true);

        // Assert - Debe lanzar ConflictException
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);

        // Verificar que solo existe un estudiante
        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findAll().get(0).getEmail()).isEqualTo("test@example.com");
    }

    /**
     * Verifica que buscar un ID inexistente lance NotFoundException
     * Oscar Chanataxi
     */
    @Test
    void inexistentIdShouldThrowNotFound() {
        // Arrange
        Long nonexistentId = 999L;

        // Act & Assert
        assertThatThrownBy(() -> service.getById(nonexistentId))
                .isInstanceOf(edu.espe.springpruebaoscarchanataxi.web.advice.NotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

    /**
     * Verifica que desactivar un estudiante establezca active en false
     * Oscar Chanataxi
     */
    @Test
    void deactivateStudentShouldSetActiveFalse() {
        // Arrange
        Student student = new Student();
        student.setFullName("Oscar Chanataxi");
        student.setEmail("ofchanataxi@espe.edu.ec");
        student.setBirthDate(LocalDate.of(1995, 5, 15));
        student.setActive(true);
        repository.save(student);

        // Act
        service.deactivate(student.getId());

        // Assert
        Student updated = repository.findById(student.getId()).orElseThrow();
        assertThat(updated.getActive()).isFalse();
    }

    /**
     * Verifica que getStats retorne las cantidades correctas de estudiantes
     * Oscar Chanataxi
     */
    @Test
    void getStatsShouldReturnCorrectCounts() {
        // Arrange - Crear 2 estudiantes activos
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

        // Arrange - Crear 1 estudiante inactivo
        Student inactiveStudent1 = new Student();
        inactiveStudent1.setFullName("Inactive One");
        inactiveStudent1.setEmail("inactive1@example.com");
        inactiveStudent1.setBirthDate(LocalDate.of(2000, 1, 1));
        inactiveStudent1.setActive(false);
        repository.save(inactiveStudent1);

        // Act
        Object stats = service.getStats();

        // Assert
        assertThat(stats).isInstanceOf(java.util.Map.class);
        java.util.Map<?, ?> statsMap = (java.util.Map<?, ?>) stats;

        assertThat(statsMap.get("total")).isEqualTo(3L);
        assertThat(statsMap.get("active")).isEqualTo(2L);
        assertThat(statsMap.get("inactive")).isEqualTo(2L);
    }
}