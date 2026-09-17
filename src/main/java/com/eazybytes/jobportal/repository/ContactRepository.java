package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findContactByStatus(String status);

    List<Contact> findContactsByStatusOrderByCreatedAtAsc(String status);

    List<Contact> findContactByStatus(String status, Sort sort);

    Page<Contact> findContactsByStatus(String status, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    int updateStatusById(@Param("id")  Long id, @Param("status") String status,
                          @Param("updateBy") String updateBy);
}