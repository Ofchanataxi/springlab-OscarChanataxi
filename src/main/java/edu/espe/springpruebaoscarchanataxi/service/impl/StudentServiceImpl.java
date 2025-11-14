package edu.espe.springpruebaoscarchanataxi.service.impl;

import edu.espe.springpruebaoscarchanataxi.domain.Student;
import edu.espe.springpruebaoscarchanataxi.dto.StudentRequestData;
import edu.espe.springpruebaoscarchanataxi.dto.StudentResponse;
import edu.espe.springpruebaoscarchanataxi.repository.StudentRepository;
import edu.espe.springpruebaoscarchanataxi.service.StudentService;
import edu.espe.springpruebaoscarchanataxi.web.advice.ConflictException;
import edu.espe.springpruebaoscarchanataxi.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;

    public StudentServiceImpl(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public StudentResponse create(StudentRequestData request) {
        if(repo.existsByEmail(request.getEmail())){
            throw new ConflictException("El email ya está registrado");
        }

        //validar que no sea fecha futura
        if(request.getBirthDate().isAfter(java.time.LocalDate.now())){
            throw new ConflictException("La fecha de nacimiento no puede ser futura");
        }

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setActive(true);

        Student saved = repo.save(student);
        return toResponse(saved);
    }

    @Override
    public StudentResponse getById(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        return toResponse(student);
    }

    @Override
    public List<StudentResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public StudentResponse deactivate(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        student.setActive(false);
        return toResponse(repo.save(student));
    }

    @Override
    public StudentResponse activate(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        student.setActive(true);
        return toResponse(repo.save(student));
    }

    @Override
    public StudentResponse update(Long id, StudentRequestData request) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        if(repo.existsByEmail(request.getEmail())){
            throw new ConflictException("El email ya está registrado");
        }
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setActive(request.getActive());
        return toResponse(repo.save(student));
    }

    @Override
    public Map<String, Long> getStats() {
        Long totalStudents = repo.count();
        Long activeStudents = repo.countByActiveTrue();
        Long inactiveStudents = repo.countByActiveFalse();

        return Map.of(
                "total", totalStudents,
                "active", activeStudents,
                "inactive", inactiveStudents
        );
    }


    private StudentResponse toResponse(Student student){
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFullName(student.getFullName());
        response.setEmail(student.getEmail());
        response.setBirthDate(student.getBirthDate());
        response.setActive(student.getActive());
        return response;
    }
}
