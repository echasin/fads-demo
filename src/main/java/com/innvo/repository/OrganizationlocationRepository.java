package com.innvo.repository;

import com.innvo.domain.Organizationlocation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Organizationlocation entity.
 */
@SuppressWarnings("unused")
public interface OrganizationlocationRepository extends JpaRepository<Organizationlocation,Long> {

}
