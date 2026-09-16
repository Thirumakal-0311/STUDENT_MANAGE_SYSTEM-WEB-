package com.college.sms.service;

import com.college.sms.dto.DashboardStatsDTO;
import com.college.sms.dto.StudentRequestDTO;
import com.college.sms.dto.StudentResponseDTO;

import java.util.List;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);

    List<StudentResponseDTO> getAllStudents(String search, String department, String gender, Integer year, String sortBy, String sortDir);

    StudentResponseDTO getStudentById(Long id);

    StudentResponseDTO getStudentByStudentId(String studentId);

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO);

    void deleteStudent(Long id);

    DashboardStatsDTO getDashboardStats();
}
