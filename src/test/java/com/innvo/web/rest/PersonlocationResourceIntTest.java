package com.innvo.web.rest;

import com.innvo.FadsiiApp;
import com.innvo.domain.Personlocation;
import com.innvo.repository.PersonlocationRepository;
import com.innvo.repository.search.PersonlocationSearchRepository;

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
 * Test class for the PersonlocationResource REST controller.
 *
 * @see PersonlocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FadsiiApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonlocationResourceIntTest {

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
    private PersonlocationRepository personlocationRepository;

    @Inject
    private PersonlocationSearchRepository personlocationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonlocationMockMvc;

    private Personlocation personlocation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonlocationResource personlocationResource = new PersonlocationResource();
        ReflectionTestUtils.setField(personlocationResource, "personlocationSearchRepository", personlocationSearchRepository);
        ReflectionTestUtils.setField(personlocationResource, "personlocationRepository", personlocationRepository);
        this.restPersonlocationMockMvc = MockMvcBuilders.standaloneSetup(personlocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personlocationSearchRepository.deleteAll();
        personlocation = new Personlocation();
        personlocation.setIsprimary(DEFAULT_ISPRIMARY);
        personlocation.setStatus(DEFAULT_STATUS);
        personlocation.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        personlocation.setLastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME);
    }

    @Test
    @Transactional
    public void createPersonlocation() throws Exception {
        int databaseSizeBeforeCreate = personlocationRepository.findAll().size();

        // Create the Personlocation

        restPersonlocationMockMvc.perform(post("/api/personlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personlocation)))
                .andExpect(status().isCreated());

        // Validate the Personlocation in the database
        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeCreate + 1);
        Personlocation testPersonlocation = personlocations.get(personlocations.size() - 1);
        assertThat(testPersonlocation.isIsprimary()).isEqualTo(DEFAULT_ISPRIMARY);
        assertThat(testPersonlocation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPersonlocation.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testPersonlocation.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);

        // Validate the Personlocation in ElasticSearch
        Personlocation personlocationEs = personlocationSearchRepository.findOne(testPersonlocation.getId());
        assertThat(personlocationEs).isEqualToComparingFieldByField(testPersonlocation);
    }

    @Test
    @Transactional
    public void checkIsprimaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = personlocationRepository.findAll().size();
        // set the field null
        personlocation.setIsprimary(null);

        // Create the Personlocation, which fails.

        restPersonlocationMockMvc.perform(post("/api/personlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personlocation)))
                .andExpect(status().isBadRequest());

        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = personlocationRepository.findAll().size();
        // set the field null
        personlocation.setStatus(null);

        // Create the Personlocation, which fails.

        restPersonlocationMockMvc.perform(post("/api/personlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personlocation)))
                .andExpect(status().isBadRequest());

        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifiedbyIsRequired() throws Exception {
        int databaseSizeBeforeTest = personlocationRepository.findAll().size();
        // set the field null
        personlocation.setLastmodifiedby(null);

        // Create the Personlocation, which fails.

        restPersonlocationMockMvc.perform(post("/api/personlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personlocation)))
                .andExpect(status().isBadRequest());

        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personlocationRepository.findAll().size();
        // set the field null
        personlocation.setLastmodifieddatetime(null);

        // Create the Personlocation, which fails.

        restPersonlocationMockMvc.perform(post("/api/personlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personlocation)))
                .andExpect(status().isBadRequest());

        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonlocations() throws Exception {
        // Initialize the database
        personlocationRepository.saveAndFlush(personlocation);

        // Get all the personlocations
        restPersonlocationMockMvc.perform(get("/api/personlocations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personlocation.getId().intValue())))
                .andExpect(jsonPath("$.[*].isprimary").value(hasItem(DEFAULT_ISPRIMARY.booleanValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }

    @Test
    @Transactional
    public void getPersonlocation() throws Exception {
        // Initialize the database
        personlocationRepository.saveAndFlush(personlocation);

        // Get the personlocation
        restPersonlocationMockMvc.perform(get("/api/personlocations/{id}", personlocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personlocation.getId().intValue()))
            .andExpect(jsonPath("$.isprimary").value(DEFAULT_ISPRIMARY.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(DEFAULT_LASTMODIFIEDDATETIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPersonlocation() throws Exception {
        // Get the personlocation
        restPersonlocationMockMvc.perform(get("/api/personlocations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonlocation() throws Exception {
        // Initialize the database
        personlocationRepository.saveAndFlush(personlocation);
        personlocationSearchRepository.save(personlocation);
        int databaseSizeBeforeUpdate = personlocationRepository.findAll().size();

        // Update the personlocation
        Personlocation updatedPersonlocation = new Personlocation();
        updatedPersonlocation.setId(personlocation.getId());
        updatedPersonlocation.setIsprimary(UPDATED_ISPRIMARY);
        updatedPersonlocation.setStatus(UPDATED_STATUS);
        updatedPersonlocation.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        updatedPersonlocation.setLastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME);

        restPersonlocationMockMvc.perform(put("/api/personlocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPersonlocation)))
                .andExpect(status().isOk());

        // Validate the Personlocation in the database
        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeUpdate);
        Personlocation testPersonlocation = personlocations.get(personlocations.size() - 1);
        assertThat(testPersonlocation.isIsprimary()).isEqualTo(UPDATED_ISPRIMARY);
        assertThat(testPersonlocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonlocation.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testPersonlocation.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);

        // Validate the Personlocation in ElasticSearch
        Personlocation personlocationEs = personlocationSearchRepository.findOne(testPersonlocation.getId());
        assertThat(personlocationEs).isEqualToComparingFieldByField(testPersonlocation);
    }

    @Test
    @Transactional
    public void deletePersonlocation() throws Exception {
        // Initialize the database
        personlocationRepository.saveAndFlush(personlocation);
        personlocationSearchRepository.save(personlocation);
        int databaseSizeBeforeDelete = personlocationRepository.findAll().size();

        // Get the personlocation
        restPersonlocationMockMvc.perform(delete("/api/personlocations/{id}", personlocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean personlocationExistsInEs = personlocationSearchRepository.exists(personlocation.getId());
        assertThat(personlocationExistsInEs).isFalse();

        // Validate the database is empty
        List<Personlocation> personlocations = personlocationRepository.findAll();
        assertThat(personlocations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersonlocation() throws Exception {
        // Initialize the database
        personlocationRepository.saveAndFlush(personlocation);
        personlocationSearchRepository.save(personlocation);

        // Search the personlocation
        restPersonlocationMockMvc.perform(get("/api/_search/personlocations?query=id:" + personlocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personlocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isprimary").value(hasItem(DEFAULT_ISPRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }
}
