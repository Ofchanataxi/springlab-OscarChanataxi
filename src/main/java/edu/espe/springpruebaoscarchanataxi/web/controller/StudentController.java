package edu.espe.springpruebaoscarchanataxi.web.controller;

import edu.espe.springpruebaoscarchanataxi.dto.StudentRequestData;
import edu.espe.springpruebaoscarchanataxi.dto.StudentResponse;
import edu.espe.springpruebaoscarchanataxi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/OscarChanataxi/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(studentService.list());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<StudentResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StudentResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.activate(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(@PathVariable Long id, @RequestBody StudentRequestData request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistics() {
        return ResponseEntity.ok(studentService.getStats());
    }
}
