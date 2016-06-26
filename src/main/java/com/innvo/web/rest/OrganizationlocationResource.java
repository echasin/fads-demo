package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Organizationlocation;
import com.innvo.repository.OrganizationlocationRepository;
import com.innvo.repository.search.OrganizationlocationSearchRepository;
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
 * REST controller for managing Organizationlocation.
 */
@RestController
@RequestMapping("/api")
public class OrganizationlocationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationlocationResource.class);
        
    @Inject
    private OrganizationlocationRepository organizationlocationRepository;
    
    @Inject
    private OrganizationlocationSearchRepository organizationlocationSearchRepository;
    
    /**
     * POST  /organizationlocations : Create a new organizationlocation.
     *
     * @param organizationlocation the organizationlocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizationlocation, or with status 400 (Bad Request) if the organizationlocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/organizationlocations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizationlocation> createOrganizationlocation(@Valid @RequestBody Organizationlocation organizationlocation) throws URISyntaxException {
        log.debug("REST request to save Organizationlocation : {}", organizationlocation);
        if (organizationlocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("organizationlocation", "idexists", "A new organizationlocation cannot already have an ID")).body(null);
        }
        Organizationlocation result = organizationlocationRepository.save(organizationlocation);
        organizationlocationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/organizationlocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("organizationlocation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organizationlocations : Updates an existing organizationlocation.
     *
     * @param organizationlocation the organizationlocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizationlocation,
     * or with status 400 (Bad Request) if the organizationlocation is not valid,
     * or with status 500 (Internal Server Error) if the organizationlocation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/organizationlocations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizationlocation> updateOrganizationlocation(@Valid @RequestBody Organizationlocation organizationlocation) throws URISyntaxException {
        log.debug("REST request to update Organizationlocation : {}", organizationlocation);
        if (organizationlocation.getId() == null) {
            return createOrganizationlocation(organizationlocation);
        }
        Organizationlocation result = organizationlocationRepository.save(organizationlocation);
        organizationlocationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("organizationlocation", organizationlocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organizationlocations : get all the organizationlocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizationlocations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/organizationlocations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organizationlocation>> getAllOrganizationlocations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Organizationlocations");
        Page<Organizationlocation> page = organizationlocationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizationlocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organizationlocations/:id : get the "id" organizationlocation.
     *
     * @param id the id of the organizationlocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizationlocation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/organizationlocations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizationlocation> getOrganizationlocation(@PathVariable Long id) {
        log.debug("REST request to get Organizationlocation : {}", id);
        Organizationlocation organizationlocation = organizationlocationRepository.findOne(id);
        return Optional.ofNullable(organizationlocation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /organizationlocations/:id : delete the "id" organizationlocation.
     *
     * @param id the id of the organizationlocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/organizationlocations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrganizationlocation(@PathVariable Long id) {
        log.debug("REST request to delete Organizationlocation : {}", id);
        organizationlocationRepository.delete(id);
        organizationlocationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("organizationlocation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/organizationlocations?query=:query : search for the organizationlocation corresponding
     * to the query.
     *
     * @param query the query of the organizationlocation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/organizationlocations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organizationlocation>> searchOrganizationlocations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Organizationlocations for query {}", query);
        Page<Organizationlocation> page = organizationlocationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/organizationlocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
