package com.softplan.transport.web.rest;

import com.softplan.transport.SoftplanApp;

import com.softplan.transport.domain.RoadType;
import com.softplan.transport.repository.RoadTypeRepository;
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
import java.util.List;


import static com.softplan.transport.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoadTypeResource REST controller.
 *
 * @see RoadTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoftplanApp.class)
public class RoadTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    @Autowired
    private RoadTypeRepository roadTypeRepository;

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

    private MockMvc restRoadTypeMockMvc;

    private RoadType roadType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoadTypeResource roadTypeResource = new RoadTypeResource(roadTypeRepository);
        this.restRoadTypeMockMvc = MockMvcBuilders.standaloneSetup(roadTypeResource)
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
    public static RoadType createEntity(EntityManager em) {
        RoadType roadType = new RoadType()
            .name(DEFAULT_NAME)
            .cost(DEFAULT_COST)
            .unit(DEFAULT_UNIT);
        return roadType;
    }

    @Before
    public void initTest() {
        roadType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoadType() throws Exception {
        int databaseSizeBeforeCreate = roadTypeRepository.findAll().size();

        // Create the RoadType
        restRoadTypeMockMvc.perform(post("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadType)))
            .andExpect(status().isCreated());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RoadType testRoadType = roadTypeList.get(roadTypeList.size() - 1);
        assertThat(testRoadType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoadType.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testRoadType.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void createRoadTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roadTypeRepository.findAll().size();

        // Create the RoadType with an existing ID
        roadType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoadTypeMockMvc.perform(post("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadType)))
            .andExpect(status().isBadRequest());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRoadTypes() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        // Get all the roadTypeList
        restRoadTypeMockMvc.perform(get("/api/road-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roadType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }
    
    @Test
    @Transactional
    public void getRoadType() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        // Get the roadType
        restRoadTypeMockMvc.perform(get("/api/road-types/{id}", roadType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roadType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoadType() throws Exception {
        // Get the roadType
        restRoadTypeMockMvc.perform(get("/api/road-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoadType() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        int databaseSizeBeforeUpdate = roadTypeRepository.findAll().size();

        // Update the roadType
        RoadType updatedRoadType = roadTypeRepository.findById(roadType.getId()).get();
        // Disconnect from session so that the updates on updatedRoadType are not directly saved in db
        em.detach(updatedRoadType);
        updatedRoadType
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .unit(UPDATED_UNIT);

        restRoadTypeMockMvc.perform(put("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRoadType)))
            .andExpect(status().isOk());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeUpdate);
        RoadType testRoadType = roadTypeList.get(roadTypeList.size() - 1);
        assertThat(testRoadType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoadType.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testRoadType.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void updateNonExistingRoadType() throws Exception {
        int databaseSizeBeforeUpdate = roadTypeRepository.findAll().size();

        // Create the RoadType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoadTypeMockMvc.perform(put("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadType)))
            .andExpect(status().isBadRequest());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoadType() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        int databaseSizeBeforeDelete = roadTypeRepository.findAll().size();

        // Delete the roadType
        restRoadTypeMockMvc.perform(delete("/api/road-types/{id}", roadType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoadType.class);
        RoadType roadType1 = new RoadType();
        roadType1.setId(1L);
        RoadType roadType2 = new RoadType();
        roadType2.setId(roadType1.getId());
        assertThat(roadType1).isEqualTo(roadType2);
        roadType2.setId(2L);
        assertThat(roadType1).isNotEqualTo(roadType2);
        roadType1.setId(null);
        assertThat(roadType1).isNotEqualTo(roadType2);
    }
}
