package com.poly.backend.mappers;

import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    TeacherDTO toDto(Teacher teacher);

    List<TeacherDTO> toDto(List<Teacher> teachers);

    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    Teacher toEntity(TeacherDTO teacherDTO);

    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    void updateFromDto(TeacherDTO teacherDTO, @MappingTarget Teacher teacher);
}
