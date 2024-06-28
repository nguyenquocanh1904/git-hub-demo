
package com.poly.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class InstructorDTO {

    private Long id;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    @NotBlank(message = "Instructor code is mandatory")
    private String maGiangVien;

    @NotBlank(message = "Personal email is mandatory")
    @Email(message = "Personal email should be valid")
    private String emailCaNhan;

    @NotBlank(message = "Edu email is mandatory")
    @Email(message = "Edu email should be valid")
    private String emailEdu;

    private String hinhDaiDien;

    @NotBlank(message = "Last name is mandatory")
    private String hoGiangVien;

    @NotNull(message = "Date of birth is mandatory")
    private Date ngaySinh;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String soDienThoai;

    @NotBlank(message = "First name is mandatory")
    private String tenGiangVien;
}
