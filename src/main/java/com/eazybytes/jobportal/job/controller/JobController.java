package com.eazybytes.jobportal.job.controller;

import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.UpdateJobApplicationDto;
import com.eazybytes.jobportal.job.service.IJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @GetMapping(path = "/employer" , version = "1.0")
    public ResponseEntity<List<JobDto>> getEmployerJobs(Authentication authentication) {
        String employeeEmail = authentication.getName();
        List<JobDto> jobs = jobService.getEmployerJobs(employeeEmail);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping(path = "/employer", version = "1.0")
    public ResponseEntity<?> createJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {
        String employeeEmail = authentication.getName();
        JobDto createdJob = jobService.createJob(jobDto,employeeEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @PatchMapping("/{jobId}/status/employer")
    public ResponseEntity<?> updateJobStatus(
            @PathVariable Long jobId,
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        String employeeEmail = authentication.getName();
        String status = requestBody.get("status");

        if(status == null || status.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message","Status is required"));
        }
        JobDto updateJobs = jobService.updateJobStatus(jobId, status.toUpperCase(), employeeEmail);
        return ResponseEntity.ok(updateJobs);

    }
    @GetMapping("/applications/{jobId}/employer")
    public ResponseEntity<List<JobApplicationDto>> getApplicationsByJobForEmployer(
            @PathVariable  Long jobId) {
        List<JobApplicationDto> applications = jobService.getApplicationsByJobForEmployer(jobId);
        return ResponseEntity.ok(applications);
    }
    @PatchMapping("/applications/employer")
    public ResponseEntity<String> updateJobApplication(
            @RequestBody @Valid UpdateJobApplicationDto updateJobApplicationDto) {
        boolean isUpdated = jobService.updateJobApplication(updateJobApplicationDto);
        if(!isUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update application");
        }
        return ResponseEntity.ok("Application updated successfully");
    }
}
