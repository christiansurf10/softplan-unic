package com.softplan.transport.web.rest;

import com.softplan.transport.SoftplanApp;

import com.softplan.transport.domain.TrajectoryHistory;
import com.softplan.transport.repository.TrajectoryHistoryRepository;
import com.softplan.transport.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.softplan.transport.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrajectoryHistoryResource REST controller.
 *
 * @see TrajectoryHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoftplanApp.class)
public class TrajectoryHistoryResourceIntTest {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TrajectoryHistoryRepository trajectoryHistoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTrajectoryHistoryMockMvc;

    private TrajectoryHistory trajectoryHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrajectoryHistoryResource trajectoryHistoryResource = new TrajectoryHistoryResource(trajectoryHistoryRepository);
        this.restTrajectoryHistoryMockMvc = MockMvcBuilders.standaloneSetup(trajectoryHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrajectoryHistory createEntity(EntityManager em) {
        TrajectoryHistory trajectoryHistory = new TrajectoryHistory()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return trajectoryHistory;
    }

    @Before
    public void initTest() {
        trajectoryHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrajectoryHistory() throws Exception {
        int databaseSizeBeforeCreate = trajectoryHistoryRepository.findAll().size();

        // Create the TrajectoryHistory
        restTrajectoryHistoryMockMvc.perform(post("/api/trajectory-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trajectoryHistory)))
            .andExpect(status().isCreated());

        // Validate the TrajectoryHistory in the database
        List<TrajectoryHistory> trajectoryHistoryList = trajectoryHistoryRepository.findAll();
        assertThat(trajectoryHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TrajectoryHistory testTrajectoryHistory = trajectoryHistoryList.get(trajectoryHistoryList.size() - 1);
        assertThat(testTrajectoryHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTrajectoryHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createTrajectoryHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trajectoryHistoryRepository.findAll().size();

        // Create the TrajectoryHistory with an existing ID
        trajectoryHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrajectoryHistoryMockMvc.perform(post("/api/trajectory-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trajectoryHistory)))
            .andExpect(status().isBadRequest());

        // Validate the TrajectoryHistory in the database
        List<TrajectoryHistory> trajectoryHistoryList = trajectoryHistoryRepository.findAll();
        assertThat(trajectoryHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTrajectoryHistories() throws Exception {
        // Initialize the database
        trajectoryHistoryRepository.saveAndFlush(trajectoryHistory);

        // Get all the trajectoryHistoryList
        restTrajectoryHistoryMockMvc.perform(get("/api/trajectory-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trajectoryHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getTrajectoryHistory() throws Exception {
        // Initialize the database
        trajectoryHistoryRepository.saveAndFlush(trajectoryHistory);

        // Get the trajectoryHistory
        restTrajectoryHistoryMockMvc.perform(get("/api/trajectory-histories/{id}", trajectoryHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trajectoryHistory.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrajectoryHistory() throws Exception {
        // Get the trajectoryHistory
        restTrajectoryHistoryMockMvc.perform(get("/api/trajectory-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrajectoryHistory() throws Exception {
        // Initialize the database
        trajectoryHistoryRepository.saveAndFlush(trajectoryHistory);

        int databaseSizeBeforeUpdate = trajectoryHistoryRepository.findAll().size();

        // Update the trajectoryHistory
        TrajectoryHistory updatedTrajectoryHistory = trajectoryHistoryRepository.findById(trajectoryHistory.getId()).get();
        // Disconnect from session so that the updates on updatedTrajectoryHistory are not directly saved in db
        em.detach(updatedTrajectoryHistory);
        updatedTrajectoryHistory
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restTrajectoryHistoryMockMvc.perform(put("/api/trajectory-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrajectoryHistory)))
            .andExpect(status().isOk());

        // Validate the TrajectoryHistory in the database
        List<TrajectoryHistory> trajectoryHistoryList = trajectoryHistoryRepository.findAll();
        assertThat(trajectoryHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrajectoryHistory testTrajectoryHistory = trajectoryHistoryList.get(trajectoryHistoryList.size() - 1);
        assertThat(testTrajectoryHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTrajectoryHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTrajectoryHistory() throws Exception {
        int databaseSizeBeforeUpdate = trajectoryHistoryRepository.findAll().size();

        // Create the TrajectoryHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrajectoryHistoryMockMvc.perform(put("/api/trajectory-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trajectoryHistory)))
            .andExpect(status().isBadRequest());

        // Validate the TrajectoryHistory in the database
        List<TrajectoryHistory> trajectoryHistoryList = trajectoryHistoryRepository.findAll();
        assertThat(trajectoryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrajectoryHistory() throws Exception {
        // Initialize the database
        trajectoryHistoryRepository.saveAndFlush(trajectoryHistory);

        int databaseSizeBeforeDelete = trajectoryHistoryRepository.findAll().size();

        // Delete the trajectoryHistory
        restTrajectoryHistoryMockMvc.perform(delete("/api/trajectory-histories/{id}", trajectoryHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TrajectoryHistory> trajectoryHistoryList = trajectoryHistoryRepository.findAll();
        assertThat(trajectoryHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrajectoryHistory.class);
        TrajectoryHistory trajectoryHistory1 = new TrajectoryHistory();
        trajectoryHistory1.setId(1L);
        TrajectoryHistory trajectoryHistory2 = new TrajectoryHistory();
        trajectoryHistory2.setId(trajectoryHistory1.getId());
        assertThat(trajectoryHistory1).isEqualTo(trajectoryHistory2);
        trajectoryHistory2.setId(2L);
        assertThat(trajectoryHistory1).isNotEqualTo(trajectoryHistory2);
        trajectoryHistory1.setId(null);
        assertThat(trajectoryHistory1).isNotEqualTo(trajectoryHistory2);
    }
}
