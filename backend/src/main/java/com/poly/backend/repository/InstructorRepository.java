package com.poly.backend.repository;

import com.poly.backend.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
Instructor findTeacherByEmailEdu(String email);
}
