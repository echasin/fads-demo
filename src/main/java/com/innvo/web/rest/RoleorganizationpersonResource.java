package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Roleorganizationperson;
import com.innvo.repository.RoleorganizationpersonRepository;
import com.innvo.repository.search.RoleorganizationpersonSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Roleorganizationperson.
 */
@RestController
@RequestMapping("/api")
public class RoleorganizationpersonResource {

    private final Logger log = LoggerFactory.getLogger(RoleorganizationpersonResource.class);
        
    @Inject
    private RoleorganizationpersonRepository roleorganizationpersonRepository;
    
    @Inject
    private RoleorganizationpersonSearchRepository roleorganizationpersonSearchRepository;
    
    /**
     * POST  /roleorganizationpeople : Create a new roleorganizationperson.
     *
     * @param roleorganizationperson the roleorganizationperson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roleorganizationperson, or with status 400 (Bad Request) if the roleorganizationperson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/roleorganizationpeople",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Roleorganizationperson> createRoleorganizationperson(@Valid @RequestBody Roleorganizationperson roleorganizationperson) throws URISyntaxException {
        log.debug("REST request to save Roleorganizationperson : {}", roleorganizationperson);
        if (roleorganizationperson.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("roleorganizationperson", "idexists", "A new roleorganizationperson cannot already have an ID")).body(null);
        }
        Roleorganizationperson result = roleorganizationpersonRepository.save(roleorganizationperson);
        roleorganizationpersonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/roleorganizationpeople/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("roleorganizationperson", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roleorganizationpeople : Updates an existing roleorganizationperson.
     *
     * @param roleorganizationperson the roleorganizationperson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roleorganizationperson,
     * or with status 400 (Bad Request) if the roleorganizationperson is not valid,
     * or with status 500 (Internal Server Error) if the roleorganizationperson couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/roleorganizationpeople",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Roleorganizationperson> updateRoleorganizationperson(@Valid @RequestBody Roleorganizationperson roleorganizationperson) throws URISyntaxException {
        log.debug("REST request to update Roleorganizationperson : {}", roleorganizationperson);
        if (roleorganizationperson.getId() == null) {
            return createRoleorganizationperson(roleorganizationperson);
        }
        Roleorganizationperson result = roleorganizationpersonRepository.save(roleorganizationperson);
        roleorganizationpersonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("roleorganizationperson", roleorganizationperson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roleorganizationpeople : get all the roleorganizationpeople.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roleorganizationpeople in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/roleorganizationpeople",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Roleorganizationperson>> getAllRoleorganizationpeople(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Roleorganizationpeople");
        Page<Roleorganizationperson> page = roleorganizationpersonRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roleorganizationpeople");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /roleorganizationpeople/:id : get the "id" roleorganizationperson.
     *
     * @param id the id of the roleorganizationperson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roleorganizationperson, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/roleorganizationpeople/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Roleorganizationperson> getRoleorganizationperson(@PathVariable Long id) {
        log.debug("REST request to get Roleorganizationperson : {}", id);
        Roleorganizationperson roleorganizationperson = roleorganizationpersonRepository.findOne(id);
        return Optional.ofNullable(roleorganizationperson)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /roleorganizationpeople/:id : delete the "id" roleorganizationperson.
     *
     * @param id the id of the roleorganizationperson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/roleorganizationpeople/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRoleorganizationperson(@PathVariable Long id) {
        log.debug("REST request to delete Roleorganizationperson : {}", id);
        roleorganizationpersonRepository.delete(id);
        roleorganizationpersonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("roleorganizationperson", id.toString())).build();
    }

    /**
     * SEARCH  /_search/roleorganizationpeople?query=:query : search for the roleorganizationperson corresponding
     * to the query.
     *
     * @param query the query of the roleorganizationperson search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/roleorganizationpeople",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Roleorganizationperson>> searchRoleorganizationpeople(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Roleorganizationpeople for query {}", query);
        Page<Roleorganizationperson> page = roleorganizationpersonSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/roleorganizationpeople");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
