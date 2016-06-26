package com.innvo.repository;

import com.innvo.domain.Roleorganizationperson;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Roleorganizationperson entity.
 */
@SuppressWarnings("unused")
public interface RoleorganizationpersonRepository extends JpaRepository<Roleorganizationperson,Long> {

}
