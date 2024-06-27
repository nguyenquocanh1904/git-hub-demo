package com.poly.backend.controller;

import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.dto.response.Response;
import com.poly.backend.exception.AppUnCheckedException;
import com.poly.backend.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
@Validated
public class TeacherController {

    private final TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    /**
     * Endpoint: GET /api/teachers
     * Chức năng: Lấy danh sách tất cả Giảng viên.
     * Xử lý: Gọi teacherService.getAllTeachers() để lấy danh sách Giảng viên từ dịch vụ.
     * Phản hồi: Trả về danh sách Giảng viên và thông điệp thành công nếu không có lỗi, hoặc phản hồi lỗi nếu có lỗi.
     */
    @GetMapping
    public ResponseEntity<Response> getAllTeachers() {
        try {
            List<TeacherDTO> teachers = teacherService.getAllTeachers();
            logger.info("Lấy toàn bộ danh sách Giảng viên thành công");
            return ResponseEntity.ok(new Response(LocalDateTime.now(), teachers, "Lấy toàn bộ danh sách Giảng viên thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            logger.error("Lỗi khi lấy toàn bộ danh sách Giảng viên: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
    /**
     * Endpoint: POST /api/teachers/upload
     * Chức năng: Tải lên tệp tin hình ảnh.
     * Xử lý: Đọc và sao chép nội dung của tệp tin từ MultipartFile vào thư mục src/main/resources/static.
     * Phản hồi: Trả về thông báo thành công nếu tải lên thành công, hoặc phản hồi lỗi nếu có lỗi.
     */
    @PostMapping("/upload")
    public ResponseEntity<Response> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                logger.warn("Không có tệp nào được chọn để tải lên");
                throw new AppUnCheckedException("Vui lòng chọn một tệp để tải lên", HttpStatus.BAD_REQUEST);
            }
            String uploadPath = "src/main/resources/static";
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            Path path = Paths.get(uploadPath);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            }

            logger.info("Tải tệp lên thành công: {}", file.getOriginalFilename());
            return ResponseEntity.ok(new Response(LocalDateTime.now(), null, "Tải tệp lên thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            logger.error("Lỗi khi tải tệp lên: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        } catch (IOException e) {
            logger.error("Lỗi IO khi tải tệp lên: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(LocalDateTime.now(), null, "Tải tệp lên thất bại", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    /**
     * Endpoint: GET /api/teachers/{id}
     * Chức năng: Lấy thông tin của một Giảng viên theo ID.
     * Xử lý: Gọi teacherService.getTeacherById(id) để lấy thông tin của Giảng viên.
     * Phản hồi: Trả về thông tin của Giảng viên nếu tồn tại, hoặc phản hồi lỗi nếu không tìm thấy Giảng viên.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response> getTeacherById(@PathVariable long id) {
        try {
            Optional<TeacherDTO> teacher = teacherService.getTeacherById(id);
            if (teacher.isPresent()) {
                logger.info("Lấy thông tin Giảng viên thành công, ID: {}", id);
                return ResponseEntity.ok(new Response(LocalDateTime.now(), teacher.get(), "Lấy thông tin Giảng viên thành công", HttpStatus.OK.value()));
            } else {
                logger.warn("Không tìm thấy Giảng viên, ID: {}", id);
                throw new AppUnCheckedException("Không tìm thấy Giảng viên", HttpStatus.NOT_FOUND);
            }
        } catch (AppUnCheckedException e) {
            logger.error("Lỗi khi lấy thông tin Giảng viên, ID: {}: {}", id, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
    /**
     * Endpoint: POST /api/teachers
     * Chức năng: Thêm một Giảng viên mới.
     * Xử lý: Gọi teacherService.addTeacher(teacherDTO) để thêm Giảng viên mới.
     * Phản hồi: Trả về thông tin của Giảng viên đã được thêm và thông báo thành công, hoặc phản hồi lỗi nếu có lỗi.
     */
    @PostMapping
    public ResponseEntity<Response> addTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        try {
            TeacherDTO addedTeacher = teacherService.addTeacher(teacherDTO);
            logger.info("Thêm Giảng viên thành công, ID: {}", addedTeacher.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response(LocalDateTime.now(), addedTeacher, "Thêm Giảng viên thành công", HttpStatus.CREATED.value()));
        } catch (AppUnCheckedException e) {
            logger.error("Lỗi khi thêm Giảng viên: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
    /**
     * Endpoint: PUT /api/teachers/{id}
     * Chức năng: Cập nhật thông tin của một Giảng viên.
     * Xử lý: Gọi teacherService.updateTeacher(id, teacherDTO) để cập nhật thông tin Giảng viên.
     * Phản hồi: Trả về thông tin của Giảng viên sau khi cập nhật và thông báo thành công, hoặc phản hồi lỗi nếu không tìm thấy Giảng viên để cập nhật.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTeacher(@PathVariable long id, @Valid @RequestBody TeacherDTO teacherDTO) {
        try {
            Optional<TeacherDTO> updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
            if (updatedTeacher.isPresent()) {
                logger.info("Cập nhật thông tin Giảng viên thành công, ID: {}", id);
                return ResponseEntity.ok(new Response(LocalDateTime.now(), updatedTeacher.get(), "Cập nhật thông tin Giảng viên thành công", HttpStatus.OK.value()));
            } else {
                logger.warn("Không tìm thấy Giảng viên để cập nhật, ID: {}", id);
                throw new AppUnCheckedException("Không tìm thấy Giảng viên để cập nhật", HttpStatus.NOT_FOUND);
            }
        } catch (AppUnCheckedException e) {
            logger.error("Lỗi khi cập nhật thông tin Giảng viên, ID: {}: {}", id, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
    /**
     * Endpoint: DELETE /api/teachers/{id}
     * Chức năng: Xóa một Giảng viên.
     * Xử lý: Gọi teacherService.deleteTeacher(id) để xóa Giảng viên.
     * Phản hồi: Trả về thông báo thành công nếu xóa thành công, hoặc phản hồi lỗi nếu không tìm thấy Giảng viên để xóa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTeacher(@PathVariable long id) {
        try {
            if (teacherService.deleteTeacher(id)) {
                logger.info("Xóa Giảng viên thành công, ID: {}", id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response(LocalDateTime.now(), null, "Xóa Giảng viên thành công", HttpStatus.NO_CONTENT.value()));
            } else {
                logger.warn("Không tìm thấy Giảng viên để xóa, ID: {}", id);
                throw new AppUnCheckedException("Không tìm thấy Giảng viên để xóa", HttpStatus.NOT_FOUND);
            }
        } catch (AppUnCheckedException e) {
            logger.error("Lỗi khi xóa Giảng viên, ID: {}: {}", id, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logger.warn("Lỗi xác thực dữ liệu: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }
}
