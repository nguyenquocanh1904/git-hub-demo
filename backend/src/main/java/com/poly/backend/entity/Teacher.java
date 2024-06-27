
package com.poly.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "teacher")
public class Teacher extends  AbstractEntity<Long> {

    @Column(name = "ma_giang_vien")
    @NotBlank(message = "Instructor code is mandatory")
    private String maGiangVien;

    @Column(name = "email_ca_nhan")
    @NotBlank(message = "Personal email is mandatory")
    @Email(message = "Personal email should be valid")
    private String emailCaNhan;

    @Column(name = "email_edu")
    @NotBlank(message = "Edu email is mandatory")
    @Email(message = "Edu email should be valid")
    private String emailEdu;

    @Column(name = "hinh_dai_dien")
    private String hinhDaiDien;

    @Column(name = "ho_giang_vien")
    @NotBlank(message = "Last name is mandatory")
    private String hoGiangVien;

    @Column(name = "ngay_sinh")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Date of birth is mandatory")
    private Date ngaySinh;

    @Column(name = "so_dien_thoai")
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String soDienThoai;

    @Column(name = "ten_giang_vien")
    @NotBlank(message = "First name is mandatory")
    private String tenGiangVien;
}
