package com.softplan.transport.web.rest;

import com.softplan.transport.SoftplanApp;

import com.softplan.transport.domain.Trajectory;
import com.softplan.transport.repository.TrajectoryRepository;
import com.softplan.transport.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.softplan.transport.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrajectoryResource REST controller.
 *
 * @see TrajectoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoftplanApp.class)
public class TrajectoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_FIRST_ROAD = 1D;
    private static final Double UPDATED_UNIT_FIRST_ROAD = 2D;

    private static final Double DEFAULT_UNIT_SECOND_ROAD = 1D;
    private static final Double UPDATED_UNIT_SECOND_ROAD = 2D;

    private static final Integer DEFAULT_LOAD_WEIGHT = 1;
    private static final Integer UPDATED_LOAD_WEIGHT = 2;

    private static final Double DEFAULT_TOTAL_COST = 1D;
    private static final Double UPDATED_TOTAL_COST = 2D;

    @Autowired
    private TrajectoryRepository trajectoryRepository;

    @Mock
    private TrajectoryRepository trajectoryRepositoryMock;

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

    private MockMvc restTrajectoryMockMvc;

    private Trajectory trajectory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrajectoryResource trajectoryResource = new TrajectoryResource(trajectoryRepository);
        this.restTrajectoryMockMvc = MockMvcBuilders.standaloneSetup(trajectoryResource)
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
    public static Trajectory createEntity(EntityManager em) {
        Trajectory trajectory = new Trajectory()
            .name(DEFAULT_NAME)
            .unitFirstRoad(DEFAULT_UNIT_FIRST_ROAD)
            .unitSecondRoad(DEFAULT_UNIT_SECOND_ROAD)
            .loadWeight(DEFAULT_LOAD_WEIGHT)
            .totalCost(DEFAULT_TOTAL_COST);
        return trajectory;
    }

    @Before
    public void initTest() {
        trajectory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrajectory() throws Exception {
        int databaseSizeBeforeCreate = trajectoryRepository.findAll().size();

        // Create the Trajectory
        restTrajectoryMockMvc.perform(post("/api/trajectories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trajectory)))
            .andExpect(status().isCreated());

        // Validate the Trajectory in the database
        List<Trajectory> trajectoryList = trajectoryRepository.findAll();
        assertThat(trajectoryList).hasSize(databaseSizeBeforeCreate + 1);
        Trajectory testTrajectory = trajectoryList.get(trajectoryList.size() - 1);
        assertThat(testTrajectory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrajectory.getUnitFirstRoad()).isEqualTo(DEFAULT_UNIT_FIRST_ROAD);
        assertThat(testTrajectory.getUnitSecondRoad()).isEqualTo(DEFAULT_UNIT_SECOND_ROAD);
        assertThat(testTrajectory.getLoadWeight()).isEqualTo(DEFAULT_LOAD_WEIGHT);
        assertThat(testTrajectory.getTotalCost()).isEqualTo(DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    public void createTrajectoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trajectoryRepository.findAll().size();

        // Create the Trajectory with an existing ID
        trajectory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrajectoryMockMvc.perform(post("/api/trajectories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trajectory)))
            .andExpect(status().isBadRequest());

        // Validate the Trajectory in the database
        List<Trajectory> trajectoryList = trajectoryRepository.findAll();
        assertThat(trajectoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTrajectories() throws Exception {
        // Initialize the database
        trajectoryRepository.saveAndFlush(trajectory);

        // Get all the trajectoryList
        restTrajectoryMockMvc.perform(get("/api/trajectories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trajectory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitFirstRoad").value(hasItem(DEFAULT_UNIT_FIRST_ROAD.doubleValue())))
            .andExpect(jsonPath("$.[*].unitSecondRoad").value(hasItem(DEFAULT_UNIT_SECOND_ROAD.doubleValue())))
            .andExpect(jsonPath("$.[*].loadWeight").value(hasItem(DEFAULT_LOAD_WEIGHT)))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(DEFAULT_TOTAL_COST.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTrajectoriesWithEagerRelationshipsIsEnabled() throws Exception {
        TrajectoryResource trajectoryResource = new TrajectoryResource(trajectoryRepositoryMock);
        when(trajectoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTrajectoryMockMvc = MockMvcBuilders.standaloneSetup(trajectoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTrajectoryMockMvc.perform(get("/api/trajectories?eagerload=true"))
        .andExpect(status().isOk());

        verify(trajectoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTrajectoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        TrajectoryResource trajectoryResource = new TrajectoryResource(trajectoryRepositoryMock);
            when(trajectoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTrajectoryMockMvc = MockMvcBuilders.standaloneSetup(trajectoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTrajectoryMockMvc.perform(get("/api/trajectories?eagerload=true"))
        .andExpect(status().isOk());

            verify(trajectoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTrajectory() throws Exception {
        // Initialize the database
        trajectoryRepository.saveAndFlush(trajectory);

        // Get the trajectory
        restTrajectoryMockMvc.perform(get("/api/trajectories/{id}", trajectory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trajectory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.unitFirstRoad").value(DEFAULT_UNIT_FIRST_ROAD.doubleValue()))
            .andExpect(jsonPath("$.unitSecondRoad").value(DEFAULT_UNIT_SECOND_ROAD.doubleValue()))
            .andExpect(jsonPath("$.loadWeight").value(DEFAULT_LOAD_WEIGHT))
            .andExpect(jsonPath("$.totalCost").value(DEFAULT_TOTAL_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTrajectory() throws Exception {
        // Get the trajectory
        restTrajectoryMockMvc.perform(get("/api/trajectories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrajectory() throws Exception {
        // Initialize the database
        trajectoryRepository.saveAndFlush(trajectory);

        int databaseSizeBeforeUpdate = trajectoryRepository.findAll().size();

        // Update the trajectory
        Trajectory updatedTrajectory = trajectoryRepository.findById(trajectory.getId()).get();
        // Disconnect from session so that the updates on updatedTrajectory are not directly saved in db
        em.detach(updatedTrajectory);
        updatedTrajectory
            .name(UPDATED_NAME)
            .unitFirstRoad(UPDATED_UNIT_FIRST_ROAD)
            .unitSecondRoad(UPDATED_UNIT_SECOND_ROAD)
            .loadWeight(UPDATED_LOAD_WEIGHT)
            .totalCost(UPDATED_TOTAL_COST);

        restTrajectoryMockMvc.perform(put("/api/trajectories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrajectory)))
            .andExpect(status().isOk());

        // Validate the Trajectory in the database
        List<Trajectory> trajectoryList = trajectoryRepository.findAll();
        assertThat(trajectoryList).hasSize(databaseSizeBeforeUpdate);
        Trajectory testTrajectory = trajectoryList.get(trajectoryList.size() - 1);
        assertThat(testTrajectory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrajectory.getUnitFirstRoad()).isEqualTo(UPDATED_UNIT_FIRST_ROAD);
        assertThat(testTrajectory.getUnitSecondRoad()).isEqualTo(UPDATED_UNIT_SECOND_ROAD);
        assertThat(testTrajectory.getLoadWeight()).isEqualTo(UPDATED_LOAD_WEIGHT);
        assertThat(testTrajectory.getTotalCost()).isEqualTo(UPDATED_TOTAL_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingTrajectory() throws Exception {
        int databaseSizeBeforeUpdate = trajectoryRepository.findAll().size();

        // Create the Trajectory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrajectoryMockMvc.perform(put("/api/trajectories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trajectory)))
            .andExpect(status().isBadRequest());

        // Validate the Trajectory in the database
        List<Trajectory> trajectoryList = trajectoryRepository.findAll();
        assertThat(trajectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrajectory() throws Exception {
        // Initialize the database
        trajectoryRepository.saveAndFlush(trajectory);

        int databaseSizeBeforeDelete = trajectoryRepository.findAll().size();

        // Delete the trajectory
        restTrajectoryMockMvc.perform(delete("/api/trajectories/{id}", trajectory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Trajectory> trajectoryList = trajectoryRepository.findAll();
        assertThat(trajectoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trajectory.class);
        Trajectory trajectory1 = new Trajectory();
        trajectory1.setId(1L);
        Trajectory trajectory2 = new Trajectory();
        trajectory2.setId(trajectory1.getId());
        assertThat(trajectory1).isEqualTo(trajectory2);
        trajectory2.setId(2L);
        assertThat(trajectory1).isNotEqualTo(trajectory2);
        trajectory1.setId(null);
        assertThat(trajectory1).isNotEqualTo(trajectory2);
    }
}
