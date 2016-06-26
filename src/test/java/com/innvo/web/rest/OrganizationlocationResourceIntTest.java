package com.innvo.web.rest;

import com.innvo.FadsiiApp;
import com.innvo.domain.Organizationlocation;
import com.innvo.repository.OrganizationlocationRepository;
import com.innvo.repository.search.OrganizationlocationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innvo.domain.enumeration.Status;

/**
 * Test class for the OrganizationlocationResource REST controller.
 *
 * @see OrganizationlocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FadsiiApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrganizationlocationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_ISPRIMARY = false;
    private static final Boolean UPDATED_ISPRIMARY = true;

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Pending;
    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LASTMODIFIEDDATETIME_STR = dateTimeFormatter.format(DEFAULT_LASTMODIFIEDDATETIME);

    @Inject
    private OrganizationlocationRepository organizationlocationRepository;

    @Inject
    private OrganizationlocationSearchRepository organizationlocationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrganizationlocationMockMvc;

    private Organizationlocation organizationlocation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizationlocationResource organizationlocationResource = new OrganizationlocationResource();
        ReflectionTestUtils.setField(organizationlocationResource, "organizationlocationSearchRepository", organizationlocationSearchRepository);
        ReflectionTestUtils.setField(organizationlocationResource, "organizationlocationRepository", organizationlocationRepository);
        this.restOrganizationlocationMockMvc = MockMvcBuilders.standaloneSetup(organizationlocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        organizationlocationSearchRepository.deleteAll();
        organizationlocation = new Organizationlocation();
        organizationlocation.setIsprimary(DEFAULT_ISPRIMARY);
        organizationlocation.setStatus(DEFAULT_STATUS);
        organizationlocation.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        organizationlocation.setLastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME);
    }

    @Test
    @Transactional
    public void createOrganizationlocation() throws Exception {
        int databaseSizeBeforeCreate = organizationlocationRepository.findAll().size();

        // Create the Organizationlocation

        restOrganizationlocationMockMvc.perform(post("/api/organizationlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationlocation)))
                .andExpect(status().isCreated());

        // Validate the Organizationlocation in the database
        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeCreate + 1);
        Organizationlocation testOrganizationlocation = organizationlocations.get(organizationlocations.size() - 1);
        assertThat(testOrganizationlocation.isIsprimary()).isEqualTo(DEFAULT_ISPRIMARY);
        assertThat(testOrganizationlocation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrganizationlocation.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testOrganizationlocation.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);

        // Validate the Organizationlocation in ElasticSearch
        Organizationlocation organizationlocationEs = organizationlocationSearchRepository.findOne(testOrganizationlocation.getId());
        assertThat(organizationlocationEs).isEqualToComparingFieldByField(testOrganizationlocation);
    }

    @Test
    @Transactional
    public void checkIsprimaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationlocationRepository.findAll().size();
        // set the field null
        organizationlocation.setIsprimary(null);

        // Create the Organizationlocation, which fails.

        restOrganizationlocationMockMvc.perform(post("/api/organizationlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationlocation)))
                .andExpect(status().isBadRequest());

        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationlocationRepository.findAll().size();
        // set the field null
        organizationlocation.setStatus(null);

        // Create the Organizationlocation, which fails.

        restOrganizationlocationMockMvc.perform(post("/api/organizationlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationlocation)))
                .andExpect(status().isBadRequest());

        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifiedbyIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationlocationRepository.findAll().size();
        // set the field null
        organizationlocation.setLastmodifiedby(null);

        // Create the Organizationlocation, which fails.

        restOrganizationlocationMockMvc.perform(post("/api/organizationlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationlocation)))
                .andExpect(status().isBadRequest());

        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationlocationRepository.findAll().size();
        // set the field null
        organizationlocation.setLastmodifieddatetime(null);

        // Create the Organizationlocation, which fails.

        restOrganizationlocationMockMvc.perform(post("/api/organizationlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationlocation)))
                .andExpect(status().isBadRequest());

        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizationlocations() throws Exception {
        // Initialize the database
        organizationlocationRepository.saveAndFlush(organizationlocation);

        // Get all the organizationlocations
        restOrganizationlocationMockMvc.perform(get("/api/organizationlocations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(organizationlocation.getId().intValue())))
                .andExpect(jsonPath("$.[*].isprimary").value(hasItem(DEFAULT_ISPRIMARY.booleanValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }

    @Test
    @Transactional
    public void getOrganizationlocation() throws Exception {
        // Initialize the database
        organizationlocationRepository.saveAndFlush(organizationlocation);

        // Get the organizationlocation
        restOrganizationlocationMockMvc.perform(get("/api/organizationlocations/{id}", organizationlocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(organizationlocation.getId().intValue()))
            .andExpect(jsonPath("$.isprimary").value(DEFAULT_ISPRIMARY.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(DEFAULT_LASTMODIFIEDDATETIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationlocation() throws Exception {
        // Get the organizationlocation
        restOrganizationlocationMockMvc.perform(get("/api/organizationlocations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationlocation() throws Exception {
        // Initialize the database
        organizationlocationRepository.saveAndFlush(organizationlocation);
        organizationlocationSearchRepository.save(organizationlocation);
        int databaseSizeBeforeUpdate = organizationlocationRepository.findAll().size();

        // Update the organizationlocation
        Organizationlocation updatedOrganizationlocation = new Organizationlocation();
        updatedOrganizationlocation.setId(organizationlocation.getId());
        updatedOrganizationlocation.setIsprimary(UPDATED_ISPRIMARY);
        updatedOrganizationlocation.setStatus(UPDATED_STATUS);
        updatedOrganizationlocation.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        updatedOrganizationlocation.setLastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME);

        restOrganizationlocationMockMvc.perform(put("/api/organizationlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationlocation)))
                .andExpect(status().isOk());

        // Validate the Organizationlocation in the database
        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeUpdate);
        Organizationlocation testOrganizationlocation = organizationlocations.get(organizationlocations.size() - 1);
        assertThat(testOrganizationlocation.isIsprimary()).isEqualTo(UPDATED_ISPRIMARY);
        assertThat(testOrganizationlocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrganizationlocation.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testOrganizationlocation.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);

        // Validate the Organizationlocation in ElasticSearch
        Organizationlocation organizationlocationEs = organizationlocationSearchRepository.findOne(testOrganizationlocation.getId());
        assertThat(organizationlocationEs).isEqualToComparingFieldByField(testOrganizationlocation);
    }

    @Test
    @Transactional
    public void deleteOrganizationlocation() throws Exception {
        // Initialize the database
        organizationlocationRepository.saveAndFlush(organizationlocation);
        organizationlocationSearchRepository.save(organizationlocation);
        int databaseSizeBeforeDelete = organizationlocationRepository.findAll().size();

        // Get the organizationlocation
        restOrganizationlocationMockMvc.perform(delete("/api/organizationlocations/{id}", organizationlocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean organizationlocationExistsInEs = organizationlocationSearchRepository.exists(organizationlocation.getId());
        assertThat(organizationlocationExistsInEs).isFalse();

        // Validate the database is empty
        List<Organizationlocation> organizationlocations = organizationlocationRepository.findAll();
        assertThat(organizationlocations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrganizationlocation() throws Exception {
        // Initialize the database
        organizationlocationRepository.saveAndFlush(organizationlocation);
        organizationlocationSearchRepository.save(organizationlocation);

        // Search the organizationlocation
        restOrganizationlocationMockMvc.perform(get("/api/_search/organizationlocations?query=id:" + organizationlocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationlocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isprimary").value(hasItem(DEFAULT_ISPRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }
}
