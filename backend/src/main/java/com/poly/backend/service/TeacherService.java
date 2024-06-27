package com.poly.backend.service;

import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.entity.Teacher;
import com.poly.backend.exception.AppUnCheckedException;
import com.poly.backend.mappers.TeacherMapper;
import com.poly.backend.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
Constructor Injection: Sử dụng @RequiredArgsConstructor để tự động inject các dependency thông qua constructor và loại bỏ @Autowired trên từng trường.

Transactional Methods: Đánh dấu các phương thức với @Transactional để Spring quản lý transaction tự động.

Optional Handling: Sử dụng Optional để tránh NullPointerException và NoSuchElementException.

Validation: Đưa ra một phương thức validateTeacherDTO() để thực hiện các logic validation trước khi lưu vào repository.
 */

@RequiredArgsConstructor
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Transactional(readOnly = true)
    public Optional<TeacherDTO> getTeacherById(Long id) {
        return teacherRepository.findById(id).map(teacherMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherDTO addTeacher(TeacherDTO teacherDTO) {
        validateTeacherDTO(teacherDTO);

        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Transactional
    public Optional<TeacherDTO> updateTeacher(long id, TeacherDTO teacherDTO) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            validateTeacherDTO(teacherDTO);
            teacherMapper.updateFromDto(teacherDTO, teacher);
            Teacher updatedTeacher = teacherRepository.save(teacher);
            return Optional.of(teacherMapper.toDto(updatedTeacher));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteTeacher(long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateTeacherDTO(TeacherDTO teacherDTO) {
        if (teacherDTO == null) {
            throw new AppUnCheckedException("TeacherDTO cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getMaGiangVien() == null || teacherDTO.getMaGiangVien().isEmpty()) {
            throw new AppUnCheckedException("Mã giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getTenGiangVien() == null || teacherDTO.getTenGiangVien().isEmpty()) {
            throw new AppUnCheckedException("Tên giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getHoGiangVien() == null || teacherDTO.getHoGiangVien().isEmpty()) {
            throw new AppUnCheckedException("Họ giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getNgaySinh() == null) {
            throw new AppUnCheckedException("Ngày sinh không được null", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getEmailCaNhan() == null || !teacherDTO.getEmailCaNhan().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new AppUnCheckedException("Email cá nhân không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getEmailEdu() != null && !teacherDTO.getEmailEdu().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new AppUnCheckedException("Email edu không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getSoDienThoai() == null || !teacherDTO.getSoDienThoai().matches("^\\d{10,11}$")) {
            throw new AppUnCheckedException("Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST);
        }

    }
}


