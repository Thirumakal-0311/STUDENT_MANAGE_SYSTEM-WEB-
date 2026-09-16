package com.college.sms.repository;

import com.college.sms.entity.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    boolean existsByStudentIdAndIdNot(String studentId, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:department IS NULL OR :department = '' OR s.department = :department) AND " +
           "(:gender IS NULL OR :gender = '' OR LOWER(s.gender) = LOWER(:gender)) AND " +
           "(:year IS NULL OR s.year = :year)")
    List<Student> findWithFilters(
            @Param("search") String search,
            @Param("department") String department,
            @Param("gender") String gender,
            @Param("year") Integer year,
            Sort sort
    );

    @Query("SELECT s.department, COUNT(s) FROM Student s GROUP BY s.department")
    List<Object[]> countByDepartmentGroup();

    @Query("SELECT s.gender, COUNT(s) FROM Student s GROUP BY s.gender")
    List<Object[]> countByGenderGroup();

    List<Student> findTop5ByOrderByIdDesc();
}
