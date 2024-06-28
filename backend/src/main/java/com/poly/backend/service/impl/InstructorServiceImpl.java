package com.poly.backend.service.impl;



import com.poly.backend.dto.InstructorDTO;


import java.util.List;

public interface InstructorServiceImpl {
    InstructorDTO getTeacherById(Long id);


    List<InstructorDTO> getAllTeachers();

    InstructorDTO addTeacher(InstructorDTO instructorDTO);

    InstructorDTO updateTeacher(long id, InstructorDTO instructorDTO);

    void deleteTeacher(long id);

}

