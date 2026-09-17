package com.eazybytes.jobportal.company.service;

import com.eazybytes.jobportal.dto.CompanyDto;

import java.util.List;

public interface ICompanyService {
    List<CompanyDto> getAllCompanies();
    boolean createCompany(CompanyDto companyDto);
    List<CompanyDto> getAllCompaniesForAdmin();
    boolean updateCompanyDetails(Long id, CompanyDto companyDto);
    void deleteCompanyById(Long id);
}
