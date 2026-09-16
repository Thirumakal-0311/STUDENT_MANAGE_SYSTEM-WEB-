package com.college.sms.service.impl;

import com.college.sms.dto.DashboardStatsDTO;
import com.college.sms.dto.StudentRequestDTO;
import com.college.sms.dto.StudentResponseDTO;
import com.college.sms.entity.Student;
import com.college.sms.exception.DuplicateResourceException;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.StudentRepository;
import com.college.sms.service.StudentService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        String studentId = requestDTO.getStudentId().trim();
        String email = requestDTO.getEmail().trim().toLowerCase();

        if (studentRepository.existsByStudentId(studentId)) {
            throw new DuplicateResourceException("Student ID '" + studentId + "' is already registered.");
        }

        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email address '" + email + "' is already registered.");
        }

        Student student = mapToEntity(requestDTO);
        student.setStudentId(studentId);
        student.setEmail(email);

        Student savedStudent = studentRepository.save(student);
        return mapToDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents(String search, String department, String gender, Integer year, String sortBy, String sortDir) {
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "id";
        Sort.Direction direction = (sortDir != null && sortDir.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Map DTO / query field names safely to entity properties
        if (sortField.equalsIgnoreCase("name") || sortField.equalsIgnoreCase("fullName")) {
            sortField = "fullName";
        } else if (sortField.equalsIgnoreCase("studentId")) {
            sortField = "studentId";
        } else if (sortField.equalsIgnoreCase("department")) {
            sortField = "department";
        } else if (sortField.equalsIgnoreCase("year")) {
            sortField = "year";
        } else if (sortField.equalsIgnoreCase("admissionDate")) {
            sortField = "admissionDate";
        } else {
            sortField = "id";
        }

        Sort sort = Sort.by(direction, sortField);

        List<Student> students = studentRepository.findWithFilters(
                (search != null && !search.isBlank()) ? search.trim() : null,
                (department != null && !department.isBlank()) ? department.trim() : null,
                (gender != null && !gender.isBlank()) ? gender.trim() : null,
                year,
                sort
        );

        return students.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return mapToDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Student ID: " + studentId));
        return mapToDTO(student);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        String studentId = requestDTO.getStudentId().trim();
        String email = requestDTO.getEmail().trim().toLowerCase();

        if (studentRepository.existsByStudentIdAndIdNot(studentId, id)) {
            throw new DuplicateResourceException("Student ID '" + studentId + "' is already used by another student.");
        }

        if (studentRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException("Email address '" + email + "' is already used by another student.");
        }

        existingStudent.setStudentId(studentId);
        existingStudent.setFullName(requestDTO.getFullName().trim());
        existingStudent.setEmail(email);
        existingStudent.setPhone(requestDTO.getPhone().trim());
        existingStudent.setDateOfBirth(requestDTO.getDateOfBirth());
        existingStudent.setGender(requestDTO.getGender().trim());
        existingStudent.setDepartment(requestDTO.getDepartment().trim());
        existingStudent.setYear(requestDTO.getYear());
        existingStudent.setSection(requestDTO.getSection() != null ? requestDTO.getSection().trim() : null);
        existingStudent.setAddress(requestDTO.getAddress() != null ? requestDTO.getAddress().trim() : null);
        existingStudent.setCity(requestDTO.getCity() != null ? requestDTO.getCity().trim() : null);
        existingStudent.setState(requestDTO.getState() != null ? requestDTO.getState().trim() : null);
        existingStudent.setPincode(requestDTO.getPincode() != null ? requestDTO.getPincode().trim() : null);
        existingStudent.setAdmissionDate(requestDTO.getAdmissionDate());

        Student updatedStudent = studentRepository.save(existingStudent);
        return mapToDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        long totalStudents = studentRepository.count();

        long maleCount = 0;
        long femaleCount = 0;
        long otherCount = 0;

        List<Object[]> genderResults = studentRepository.countByGenderGroup();
        for (Object[] result : genderResults) {
            String gender = (result[0] != null) ? result[0].toString().trim().toLowerCase() : "";
            long count = (result[1] instanceof Number) ? ((Number) result[1]).longValue() : 0;
            if (gender.equalsIgnoreCase("male")) {
                maleCount += count;
            } else if (gender.equalsIgnoreCase("female")) {
                femaleCount += count;
            } else {
                otherCount += count;
            }
        }

        Map<String, Long> departmentCountMap = new LinkedHashMap<>();
        List<Object[]> departmentResults = studentRepository.countByDepartmentGroup();
        for (Object[] result : departmentResults) {
            String dept = (result[0] != null) ? result[0].toString().trim() : "Unassigned";
            long count = (result[1] instanceof Number) ? ((Number) result[1]).longValue() : 0;
            departmentCountMap.put(dept, count);
        }

        List<StudentResponseDTO> recentStudents = studentRepository.findTop5ByOrderByIdDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new DashboardStatsDTO(totalStudents, maleCount, femaleCount, otherCount, departmentCountMap, recentStudents);
    }

    private Student mapToEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setDepartment(dto.getDepartment());
        student.setYear(dto.getYear());
        student.setSection(dto.getSection());
        student.setAddress(dto.getAddress());
        student.setCity(dto.getCity());
        student.setState(dto.getState());
        student.setPincode(dto.getPincode());
        student.setAdmissionDate(dto.getAdmissionDate());
        return student;
    }

    private StudentResponseDTO mapToDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setGender(student.getGender());
        dto.setDepartment(student.getDepartment());
        dto.setYear(student.getYear());
        dto.setSection(student.getSection());
        dto.setAddress(student.getAddress());
        dto.setCity(student.getCity());
        dto.setState(student.getState());
        dto.setPincode(student.getPincode());
        dto.setAdmissionDate(student.getAdmissionDate());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        return dto;
    }
}
