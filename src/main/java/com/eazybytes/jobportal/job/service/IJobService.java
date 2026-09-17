package com.eazybytes.jobportal.job.service;
import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.UpdateJobApplicationDto;

import java.util.List;

public interface IJobService {

    List<JobDto> getEmployerJobs(String employeeEmail);
    JobDto updateJobStatus(Long jobId, String status, String employeeEmail);
    JobDto createJob(JobDto jobDto, String employeeEmail);
    List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId);

    boolean updateJobApplication(UpdateJobApplicationDto updateJobApplicationDto);
}
