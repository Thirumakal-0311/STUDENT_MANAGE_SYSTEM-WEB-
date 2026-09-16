package com.college.sms.controller;

import com.college.sms.dto.ApiResponse;
import com.college.sms.dto.DashboardStatsDTO;
import com.college.sms.dto.StudentRequestDTO;
import com.college.sms.dto.StudentResponseDTO;
import com.college.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Create a new student record
     * POST /api/students
     */
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO createdStudent = studentService.createStudent(requestDTO);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    /**
     * Retrieve all students with optional search, filter, and sort capabilities
     * GET /api/students
     */
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        List<StudentResponseDTO> students = studentService.getAllStudents(search, department, gender, year, sortBy, sortDir);
        return ResponseEntity.ok(students);
    }

    /**
     * Retrieve a student by database primary key ID
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * Retrieve a student by college student ID (e.g., STU001)
     * GET /api/students/student-id/{studentId}
     */
    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentByStudentId(@PathVariable String studentId) {
        StudentResponseDTO student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(student);
    }

    /**
     * Update an existing student record
     * PUT /api/students/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Delete a student by ID
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok("Student with ID " + id + " has been successfully deleted."));
    }

    /**
     * Retrieve aggregated statistics for Dashboard analytics
     * GET /api/students/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = studentService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}
