package edu.espe.springpruebaoscarchanataxi.service;

import edu.espe.springpruebaoscarchanataxi.dto.StudentRequestData;
import edu.espe.springpruebaoscarchanataxi.dto.StudentResponse;

import java.util.List;

public interface StudentService {

    //Crear un estudiante a partir del DTO validado
    StudentResponse create(StudentRequestData request);

    //Búsqueda por ID
    StudentResponse getById(Long id);

    //Listar todos los estudiantes
    List<StudentResponse> list();

    //Cambiar estado del estudiante a falso
    StudentResponse deactivate(Long id);

    //Cambiar el estado del estudainte a verdadero
    StudentResponse activate(Long id);

    //Actializar todo el estudiante
    StudentResponse update(Long id, StudentRequestData request);

    //Estadisticas
    Object getStatistics();
}
