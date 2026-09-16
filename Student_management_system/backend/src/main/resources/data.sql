-- Spring Boot initialization data script
INSERT IGNORE INTO students (id, student_id, full_name, email, phone, date_of_birth, gender, department, year, section, address, city, state, pincode, admission_date)
VALUES
(1, 'STU001', 'Arun Kumar', 'arun.kumar@example.com', '9876543210', '2005-06-15', 'Male', 'Information Technology', 2, 'A', '12 Main Road', 'Thanjavur', 'Tamil Nadu', '613001', '2024-06-10'),
(2, 'STU002', 'Priya Sharma', 'priya.sharma@example.com', '9845123456', '2004-03-22', 'Female', 'Computer Science', 3, 'B', '45 Gandhi Street', 'Chennai', 'Tamil Nadu', '600028', '2023-07-01'),
(3, 'STU003', 'Karthik Raja', 'karthik.raja@example.com', '9789012345', '2006-11-09', 'Male', 'Electronics & Communication', 1, 'A', '89 Park Avenue', 'Coimbatore', 'Tamil Nadu', '641004', '2025-06-18'),
(4, 'STU004', 'Deepa Lakshmi', 'deepa.lakshmi@example.com', '9443216789', '2003-08-30', 'Female', 'Computer Science', 4, 'A', '23 Temple View', 'Madurai', 'Tamil Nadu', '625001', '2022-07-15'),
(5, 'STU005', 'Sam Alex', 'sam.alex@example.com', '9871234560', '2005-01-14', 'Other', 'Mechanical Engineering', 2, 'B', '104 Lake Side', 'Kochi', 'Kerala', '682001', '2024-06-12'),
(6, 'STU006', 'Sneha Patel', 'sneha.patel@example.com', '9823456781', '2004-09-19', 'Female', 'Information Technology', 3, 'A', '56 Ring Road', 'Ahmedabad', 'Gujarat', '380015', '2023-06-25'),
(7, 'STU007', 'Vignesh Sundaram', 'vignesh.s@example.com', '9712345678', '2006-04-05', 'Male', 'Civil Engineering', 1, 'C', '78 River Bank', 'Trichy', 'Tamil Nadu', '620002', '2025-07-02'),
(8, 'STU008', 'Ananya Roy', 'ananya.roy@example.com', '9903456712', '2003-12-12', 'Female', 'Electronics & Communication', 4, 'B', '14 Salt Lake', 'Kolkata', 'West Bengal', '700064', '2022-08-01');
