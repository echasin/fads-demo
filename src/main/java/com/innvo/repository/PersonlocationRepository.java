package com.innvo.repository;

import com.innvo.domain.Personlocation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Personlocation entity.
 */
@SuppressWarnings("unused")
public interface PersonlocationRepository extends JpaRepository<Personlocation,Long> {

}
