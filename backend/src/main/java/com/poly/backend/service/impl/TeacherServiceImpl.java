package com.poly.backend.service.impl;



import com.poly.backend.dto.TeacherDTO;


import java.util.List;

public interface TeacherServiceImpl {
    TeacherDTO getTeacherById(Long id);


    List<TeacherDTO> getAllTeachers();

    TeacherDTO addTeacher(TeacherDTO teacherDTO);

    TeacherDTO updateTeacher(long id, TeacherDTO teacherDTO);

    void deleteTeacher(long id);

}

