package com.poly.backend.service;

import com.poly.backend.dto.InstructorDTO;
import com.poly.backend.entity.Instructor;
import com.poly.backend.exception.AppUnCheckedException;
import com.poly.backend.mapper.InstructorMapper;
import com.poly.backend.repository.InstructorRepository;
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
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    @Transactional(readOnly = true)
    public Optional<InstructorDTO> getTeacherById(Long id) {
        return instructorRepository.findById(id).map(instructorMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<InstructorDTO> getAllTeachers() {
        return instructorRepository.findAll().stream()
                .map(instructorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstructorDTO addTeacher(InstructorDTO instructorDTO) {
        validateTeacherDTO(instructorDTO);

        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        Instructor savedInstructor = instructorRepository.save(instructor);
        return instructorMapper.toDto(savedInstructor);
    }

    @Transactional
    public Optional<InstructorDTO> updateTeacher(long id, InstructorDTO instructorDTO) {
        Optional<Instructor> optionalTeacher = instructorRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Instructor instructor = optionalTeacher.get();
            validateTeacherDTO(instructorDTO);
            instructorMapper.updateFromDto(instructorDTO, instructor);
            Instructor updatedInstructor = instructorRepository.save(instructor);
            return Optional.of(instructorMapper.toDto(updatedInstructor));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteTeacher(long id) {
        if (instructorRepository.existsById(id)) {
            instructorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateTeacherDTO(InstructorDTO instructorDTO) {
        if (instructorDTO == null) {
            throw new AppUnCheckedException("InstructorDTO cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getMaGiangVien() == null || instructorDTO.getMaGiangVien().isEmpty()) {
            throw new AppUnCheckedException("Mã giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getTenGiangVien() == null || instructorDTO.getTenGiangVien().isEmpty()) {
            throw new AppUnCheckedException("Tên giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getHoGiangVien() == null || instructorDTO.getHoGiangVien().isEmpty()) {
            throw new AppUnCheckedException("Họ giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getNgaySinh() == null) {
            throw new AppUnCheckedException("Ngày sinh không được null", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getEmailCaNhan() == null || !instructorDTO.getEmailCaNhan().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new AppUnCheckedException("Email cá nhân không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getEmailEdu() != null && !instructorDTO.getEmailEdu().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new AppUnCheckedException("Email edu không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        if (instructorDTO.getSoDienThoai() == null || !instructorDTO.getSoDienThoai().matches("^\\d{10,11}$")) {
            throw new AppUnCheckedException("Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST);
        }

    }
}


