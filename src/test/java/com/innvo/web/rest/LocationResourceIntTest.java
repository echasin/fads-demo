package com.innvo.web.rest;

import com.innvo.FadsiiApp;
import com.innvo.domain.Location;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.search.LocationSearchRepository;

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
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FadsiiApp.class)
@WebAppConfiguration
@IntegrationTest
public class LocationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ADDRESSLINE_1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ADDRESSLINE_1 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ADDRESSLINE_2 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ADDRESSLINE_2 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CITY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_STATECODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_STATECODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ZIPPOSTCODE = "AAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ZIPPOSTCODE = "BBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Pending;
    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LASTMODIFIEDDATETIME_STR = dateTimeFormatter.format(DEFAULT_LASTMODIFIEDDATETIME);

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private LocationSearchRepository locationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocationMockMvc;

    private Location location;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationResource locationResource = new LocationResource();
        ReflectionTestUtils.setField(locationResource, "locationSearchRepository", locationSearchRepository);
        ReflectionTestUtils.setField(locationResource, "locationRepository", locationRepository);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        locationSearchRepository.deleteAll();
        location = new Location();
        location.setAddressline1(DEFAULT_ADDRESSLINE_1);
        location.setAddressline2(DEFAULT_ADDRESSLINE_2);
        location.setCity(DEFAULT_CITY);
        location.setStatecode(DEFAULT_STATECODE);
        location.setZippostcode(DEFAULT_ZIPPOSTCODE);
        location.setStatus(DEFAULT_STATUS);
        location.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        location.setLastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getAddressline1()).isEqualTo(DEFAULT_ADDRESSLINE_1);
        assertThat(testLocation.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLocation.getStatecode()).isEqualTo(DEFAULT_STATECODE);
        assertThat(testLocation.getZippostcode()).isEqualTo(DEFAULT_ZIPPOSTCODE);
        assertThat(testLocation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLocation.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testLocation.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);

        // Validate the Location in ElasticSearch
        Location locationEs = locationSearchRepository.findOne(testLocation.getId());
        assertThat(locationEs).isEqualToComparingFieldByField(testLocation);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setStatus(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifiedbyIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLastmodifiedby(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLastmodifieddatetime(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locations
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
                .andExpect(jsonPath("$.[*].addressline1").value(hasItem(DEFAULT_ADDRESSLINE_1.toString())))
                .andExpect(jsonPath("$.[*].addressline2").value(hasItem(DEFAULT_ADDRESSLINE_2.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].statecode").value(hasItem(DEFAULT_STATECODE.toString())))
                .andExpect(jsonPath("$.[*].zippostcode").value(hasItem(DEFAULT_ZIPPOSTCODE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }

    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.addressline1").value(DEFAULT_ADDRESSLINE_1.toString()))
            .andExpect(jsonPath("$.addressline2").value(DEFAULT_ADDRESSLINE_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.statecode").value(DEFAULT_STATECODE.toString()))
            .andExpect(jsonPath("$.zippostcode").value(DEFAULT_ZIPPOSTCODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(DEFAULT_LASTMODIFIEDDATETIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        locationSearchRepository.save(location);
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = new Location();
        updatedLocation.setId(location.getId());
        updatedLocation.setAddressline1(UPDATED_ADDRESSLINE_1);
        updatedLocation.setAddressline2(UPDATED_ADDRESSLINE_2);
        updatedLocation.setCity(UPDATED_CITY);
        updatedLocation.setStatecode(UPDATED_STATECODE);
        updatedLocation.setZippostcode(UPDATED_ZIPPOSTCODE);
        updatedLocation.setStatus(UPDATED_STATUS);
        updatedLocation.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        updatedLocation.setLastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME);

        restLocationMockMvc.perform(put("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocation)))
                .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testLocation.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocation.getStatecode()).isEqualTo(UPDATED_STATECODE);
        assertThat(testLocation.getZippostcode()).isEqualTo(UPDATED_ZIPPOSTCODE);
        assertThat(testLocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocation.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testLocation.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);

        // Validate the Location in ElasticSearch
        Location locationEs = locationSearchRepository.findOne(testLocation.getId());
        assertThat(locationEs).isEqualToComparingFieldByField(testLocation);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        locationSearchRepository.save(location);
        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Get the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean locationExistsInEs = locationSearchRepository.exists(location.getId());
        assertThat(locationExistsInEs).isFalse();

        // Validate the database is empty
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        locationSearchRepository.save(location);

        // Search the location
        restLocationMockMvc.perform(get("/api/_search/locations?query=id:" + location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressline1").value(hasItem(DEFAULT_ADDRESSLINE_1.toString())))
            .andExpect(jsonPath("$.[*].addressline2").value(hasItem(DEFAULT_ADDRESSLINE_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].statecode").value(hasItem(DEFAULT_STATECODE.toString())))
            .andExpect(jsonPath("$.[*].zippostcode").value(hasItem(DEFAULT_ZIPPOSTCODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(DEFAULT_LASTMODIFIEDDATETIME_STR)));
    }
}
