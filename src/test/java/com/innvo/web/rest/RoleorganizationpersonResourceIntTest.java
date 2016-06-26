package com.innvo.web.rest;

import com.innvo.FadsiiApp;
import com.innvo.domain.Roleorganizationperson;
import com.innvo.repository.RoleorganizationpersonRepository;
import com.innvo.repository.search.RoleorganizationpersonSearchRepository;

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
 * Test class for the RoleorganizationpersonResource REST controller.
 *
 * @see RoleorganizationpersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FadsiiApp.class)
@WebAppConfiguration
@IntegrationTest
public class RoleorganizationpersonResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ROLE = "AAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Pending;
    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LASTMODIFIEDDATETIME_STR = dateTimeFormatter.format(DEFAULT_LASTMODIFIEDDATETIME);

    @Inject
    private RoleorganizationpersonRepository roleorganizationpersonRepository;

    @Inject
    private RoleorganizationpersonSearchRepository roleorganizationpersonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRoleorganizationpersonMockMvc;

    private Roleorganizationperson roleorganizationperson;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoleorganizationpersonResource roleorganizationpersonResource = new RoleorganizationpersonResource();
        ReflectionTestUtils.setField(roleorganizationpersonResource, "roleorganizationpersonSearchRepository", roleorganizationpersonSearchRepository);
        ReflectionTestUtils.setField(roleorganizationpersonResource, "roleorganizationpersonRepository", roleorganizationpersonRepository);
        this.restRoleorganizationpersonMockMvc = MockMvcBuilders.standaloneSetup(roleorganizationpersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        roleorganizationpersonSearchRepository.deleteAll();
        roleorganizationperson = new Roleorganizationperson();
        roleorganizationperson.setRole(DEFAULT_ROLE);
        roleorganizationperson.setStatus(DEFAULT_STATUS);
        roleorganizationperson.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        roleorganizationperson.setLastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME);
    }

    @Test
    @Transactional
    public void createRoleorganizationperson() throws Exception {
        int databaseSizeBeforeCreate = roleorganizationpersonRepository.findAll().size();

        // Create the Roleorganizationperson

        restRoleorganizationpersonMockMvc.perform(post("/api/roleorganizationpeople")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleorganizationperson)))
                .andExpect(status().isCreated());

        // Validate the Roleorganizationperson in the database
        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeCreate + 1);
        Roleorganizationperson testRoleorganizationperson = roleorganizationpeople.get(roleorganizationpeople.size() - 1);
        assertThat(testRoleorganizationperson.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testRoleorganizationperson.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRoleorganizationperson.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testRoleorganizationperson.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);

        // Validate the Roleorganizationperson in ElasticSearch
        Roleorganizationperson roleorganizationpersonEs = roleorganizationpersonSearchRepository.findOne(testRoleorganizationperson.getId());
        assertThat(roleorganizationpersonEs).isEqualToComparingFieldByField(testRoleorganizationperson);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleorganizationpersonRepository.findAll().size();
        // set the field null
        roleorganizationperson.setRole(null);

        // Create the Roleorganizationperson, which fails.

        restRoleorganizationpersonMockMvc.perform(post("/api/roleorganizationpeople")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleorganizationperson)))
                .andExpect(status().isBadRequest());

        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleorganizationpersonRepository.findAll().size();
        // set the field null
        roleorganizationperson.setStatus(null);

        // Create the Roleorganizationperson, which fails.

        restRoleorganizationpersonMockMvc.perform(post("/api/roleorganizationpeople")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleorganizationperson)))
                .andExpect(status().isBadRequest());

        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifiedbyIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleorganizationpersonRepository.findAll().size();
        // set the field null
        roleorganizationperson.setLastmodifiedby(null);

        // Create the Roleorganizationperson, which fails.

        restRoleorganizationpersonMockMvc.perform(post("/api/roleorganizationpeople")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleorganizationperson)))
                .andExpect(status().isBadRequest());

        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleorganizationpersonRepository.findAll().size();
        // set the field null
        roleorganizationperson.setLastmodifieddatetime(null);

        // Create the Roleorganizationperson, which fails.

        restRoleorganizationpersonMockMvc.perform(post("/api/roleorganizationpeople")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleorganizationperson)))
                .andExpect(status().isBadRequest());

        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoleorganizationpeople() throws Exception {
        // Initialize the database
        roleorganizationpersonRepository.saveAndFlush(roleorganizationperson);

        // Get all the roleorganizationpeople
        restRoleorganizationpersonMockMvc.perform(get("/api/roleorganizationpeople?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(roleorganizationperson.getId().intValue())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }

    @Test
    @Transactional
    public void getRoleorganizationperson() throws Exception {
        // Initialize the database
        roleorganizationpersonRepository.saveAndFlush(roleorganizationperson);

        // Get the roleorganizationperson
        restRoleorganizationpersonMockMvc.perform(get("/api/roleorganizationpeople/{id}", roleorganizationperson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(roleorganizationperson.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(DEFAULT_LASTMODIFIEDDATETIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingRoleorganizationperson() throws Exception {
        // Get the roleorganizationperson
        restRoleorganizationpersonMockMvc.perform(get("/api/roleorganizationpeople/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoleorganizationperson() throws Exception {
        // Initialize the database
        roleorganizationpersonRepository.saveAndFlush(roleorganizationperson);
        roleorganizationpersonSearchRepository.save(roleorganizationperson);
        int databaseSizeBeforeUpdate = roleorganizationpersonRepository.findAll().size();

        // Update the roleorganizationperson
        Roleorganizationperson updatedRoleorganizationperson = new Roleorganizationperson();
        updatedRoleorganizationperson.setId(roleorganizationperson.getId());
        updatedRoleorganizationperson.setRole(UPDATED_ROLE);
        updatedRoleorganizationperson.setStatus(UPDATED_STATUS);
        updatedRoleorganizationperson.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        updatedRoleorganizationperson.setLastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME);

        restRoleorganizationpersonMockMvc.perform(put("/api/roleorganizationpeople")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRoleorganizationperson)))
                .andExpect(status().isOk());

        // Validate the Roleorganizationperson in the database
        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeUpdate);
        Roleorganizationperson testRoleorganizationperson = roleorganizationpeople.get(roleorganizationpeople.size() - 1);
        assertThat(testRoleorganizationperson.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testRoleorganizationperson.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRoleorganizationperson.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testRoleorganizationperson.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);

        // Validate the Roleorganizationperson in ElasticSearch
        Roleorganizationperson roleorganizationpersonEs = roleorganizationpersonSearchRepository.findOne(testRoleorganizationperson.getId());
        assertThat(roleorganizationpersonEs).isEqualToComparingFieldByField(testRoleorganizationperson);
    }

    @Test
    @Transactional
    public void deleteRoleorganizationperson() throws Exception {
        // Initialize the database
        roleorganizationpersonRepository.saveAndFlush(roleorganizationperson);
        roleorganizationpersonSearchRepository.save(roleorganizationperson);
        int databaseSizeBeforeDelete = roleorganizationpersonRepository.findAll().size();

        // Get the roleorganizationperson
        restRoleorganizationpersonMockMvc.perform(delete("/api/roleorganizationpeople/{id}", roleorganizationperson.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean roleorganizationpersonExistsInEs = roleorganizationpersonSearchRepository.exists(roleorganizationperson.getId());
        assertThat(roleorganizationpersonExistsInEs).isFalse();

        // Validate the database is empty
        List<Roleorganizationperson> roleorganizationpeople = roleorganizationpersonRepository.findAll();
        assertThat(roleorganizationpeople).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRoleorganizationperson() throws Exception {
        // Initialize the database
        roleorganizationpersonRepository.saveAndFlush(roleorganizationperson);
        roleorganizationpersonSearchRepository.save(roleorganizationperson);

        // Search the roleorganizationperson
        restRoleorganizationpersonMockMvc.perform(get("/api/_search/roleorganizationpeople?query=id:" + roleorganizationperson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleorganizationperson.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }
}
