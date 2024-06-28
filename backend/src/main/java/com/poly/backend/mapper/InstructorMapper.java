package com.poly.backend.mapper;

import com.poly.backend.dto.InstructorDTO;
import com.poly.backend.entity.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    InstructorMapper INSTANCE = Mappers.getMapper(InstructorMapper.class);

    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    InstructorDTO toDto(Instructor instructor);

    List<InstructorDTO> toDto(List<Instructor> instructors);

    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    Instructor toEntity(InstructorDTO instructorDTO);

    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    void updateFromDto(InstructorDTO instructorDTO, @MappingTarget Instructor instructor);
}
