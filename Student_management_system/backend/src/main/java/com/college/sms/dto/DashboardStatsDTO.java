package com.college.sms.dto;

import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {

    private long totalStudents;
    private long maleStudents;
    private long femaleStudents;
    private long otherStudents;
    private Map<String, Long> departmentWiseCount;
    private List<StudentResponseDTO> recentStudents;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(long totalStudents, long maleStudents, long femaleStudents,
                             long otherStudents, Map<String, Long> departmentWiseCount,
                             List<StudentResponseDTO> recentStudents) {
        this.totalStudents = totalStudents;
        this.maleStudents = maleStudents;
        this.femaleStudents = femaleStudents;
        this.otherStudents = otherStudents;
        this.departmentWiseCount = departmentWiseCount;
        this.recentStudents = recentStudents;
    }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getMaleStudents() { return maleStudents; }
    public void setMaleStudents(long maleStudents) { this.maleStudents = maleStudents; }

    public long getFemaleStudents() { return femaleStudents; }
    public void setFemaleStudents(long femaleStudents) { this.femaleStudents = femaleStudents; }

    public long getOtherStudents() { return otherStudents; }
    public void setOtherStudents(long otherStudents) { this.otherStudents = otherStudents; }

    public Map<String, Long> getDepartmentWiseCount() { return departmentWiseCount; }
    public void setDepartmentWiseCount(Map<String, Long> departmentWiseCount) { this.departmentWiseCount = departmentWiseCount; }

    public List<StudentResponseDTO> getRecentStudents() { return recentStudents; }
    public void setRecentStudents(List<StudentResponseDTO> recentStudents) { this.recentStudents = recentStudents; }
}
