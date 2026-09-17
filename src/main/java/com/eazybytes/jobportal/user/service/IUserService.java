package com.eazybytes.jobportal.user.service;

import com.eazybytes.jobportal.dto.ApplyJobRequestDto;
import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.ProfileDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<UserDto> searchUserByEmail(String email);
    UserDto elevateToEmployer(Long userId);
    UserDto assignCompanyToEmployer(Long userId, Long companyId);
    ProfileDto createOrUpdateProfile(String userEmail, String profileJson, MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException;
    ProfileDto getProfile(String userEmail);
    ProfileDto getProfilePicture(String userEmail);
    ProfileDto getResume(String userEmail);
    JobDto saveJob(String userEmail, Long jobId);
    void unsavedJob(String userEmail, Long jobId);
    List<JobDto> getSavedJobs(String userEmail);
    JobApplicationDto applyForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto);
    void withdrawApplication(String userEmail, Long jobId);
    List<JobApplicationDto> getJobSeekerApplications(String userEmail);
}
