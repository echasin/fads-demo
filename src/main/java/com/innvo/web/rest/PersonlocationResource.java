package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Personlocation;
import com.innvo.repository.PersonlocationRepository;
import com.innvo.repository.search.PersonlocationSearchRepository;
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
 * REST controller for managing Personlocation.
 */
@RestController
@RequestMapping("/api")
public class PersonlocationResource {

    private final Logger log = LoggerFactory.getLogger(PersonlocationResource.class);
        
    @Inject
    private PersonlocationRepository personlocationRepository;
    
    @Inject
    private PersonlocationSearchRepository personlocationSearchRepository;
    
    /**
     * POST  /personlocations : Create a new personlocation.
     *
     * @param personlocation the personlocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personlocation, or with status 400 (Bad Request) if the personlocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/personlocations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personlocation> createPersonlocation(@Valid @RequestBody Personlocation personlocation) throws URISyntaxException {
        log.debug("REST request to save Personlocation : {}", personlocation);
        if (personlocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personlocation", "idexists", "A new personlocation cannot already have an ID")).body(null);
        }
        Personlocation result = personlocationRepository.save(personlocation);
        personlocationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/personlocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personlocation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personlocations : Updates an existing personlocation.
     *
     * @param personlocation the personlocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personlocation,
     * or with status 400 (Bad Request) if the personlocation is not valid,
     * or with status 500 (Internal Server Error) if the personlocation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/personlocations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personlocation> updatePersonlocation(@Valid @RequestBody Personlocation personlocation) throws URISyntaxException {
        log.debug("REST request to update Personlocation : {}", personlocation);
        if (personlocation.getId() == null) {
            return createPersonlocation(personlocation);
        }
        Personlocation result = personlocationRepository.save(personlocation);
        personlocationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personlocation", personlocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personlocations : get all the personlocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of personlocations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/personlocations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Personlocation>> getAllPersonlocations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Personlocations");
        Page<Personlocation> page = personlocationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personlocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /personlocations/:id : get the "id" personlocation.
     *
     * @param id the id of the personlocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personlocation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/personlocations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personlocation> getPersonlocation(@PathVariable Long id) {
        log.debug("REST request to get Personlocation : {}", id);
        Personlocation personlocation = personlocationRepository.findOne(id);
        return Optional.ofNullable(personlocation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personlocations/:id : delete the "id" personlocation.
     *
     * @param id the id of the personlocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/personlocations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonlocation(@PathVariable Long id) {
        log.debug("REST request to delete Personlocation : {}", id);
        personlocationRepository.delete(id);
        personlocationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personlocation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/personlocations?query=:query : search for the personlocation corresponding
     * to the query.
     *
     * @param query the query of the personlocation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/personlocations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Personlocation>> searchPersonlocations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Personlocations for query {}", query);
        Page<Personlocation> page = personlocationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/personlocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
