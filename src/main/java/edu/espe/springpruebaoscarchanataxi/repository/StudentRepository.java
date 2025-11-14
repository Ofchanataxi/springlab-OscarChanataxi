package edu.espe.springpruebaoscarchanataxi.repository;

import edu.espe.springpruebaoscarchanataxi.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    //Buscar un estudiante por email
    Optional<Student> findByEmail(String email);
    //Responder si existe el estudiante con ese email
    boolean existsByEmail(String email);
    //Contar estudiantes activos
    long countByActiveTrue();
    //Contar estudiantes inactivos
    long countByActiveFalse();
    //buscar estiudiante por ID
}
