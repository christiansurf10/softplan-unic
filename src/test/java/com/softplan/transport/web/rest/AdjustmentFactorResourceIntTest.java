package com.softplan.transport.web.rest;

import com.softplan.transport.SoftplanApp;

import com.softplan.transport.domain.AdjustmentFactor;
import com.softplan.transport.repository.AdjustmentFactorRepository;
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
 * Test class for the AdjustmentFactorResource REST controller.
 *
 * @see AdjustmentFactorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoftplanApp.class)
public class AdjustmentFactorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_OPERATOR = "BBBBBBBBBB";

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    @Autowired
    private AdjustmentFactorRepository adjustmentFactorRepository;

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

    private MockMvc restAdjustmentFactorMockMvc;

    private AdjustmentFactor adjustmentFactor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdjustmentFactorResource adjustmentFactorResource = new AdjustmentFactorResource(adjustmentFactorRepository);
        this.restAdjustmentFactorMockMvc = MockMvcBuilders.standaloneSetup(adjustmentFactorResource)
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
    public static AdjustmentFactor createEntity(EntityManager em) {
        AdjustmentFactor adjustmentFactor = new AdjustmentFactor()
            .name(DEFAULT_NAME)
            .condition(DEFAULT_CONDITION)
            .conditionOperator(DEFAULT_CONDITION_OPERATOR)
            .cost(DEFAULT_COST);
        return adjustmentFactor;
    }

    @Before
    public void initTest() {
        adjustmentFactor = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdjustmentFactor() throws Exception {
        int databaseSizeBeforeCreate = adjustmentFactorRepository.findAll().size();

        // Create the AdjustmentFactor
        restAdjustmentFactorMockMvc.perform(post("/api/adjustment-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentFactor)))
            .andExpect(status().isCreated());

        // Validate the AdjustmentFactor in the database
        List<AdjustmentFactor> adjustmentFactorList = adjustmentFactorRepository.findAll();
        assertThat(adjustmentFactorList).hasSize(databaseSizeBeforeCreate + 1);
        AdjustmentFactor testAdjustmentFactor = adjustmentFactorList.get(adjustmentFactorList.size() - 1);
        assertThat(testAdjustmentFactor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdjustmentFactor.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testAdjustmentFactor.getConditionOperator()).isEqualTo(DEFAULT_CONDITION_OPERATOR);
        assertThat(testAdjustmentFactor.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    public void createAdjustmentFactorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adjustmentFactorRepository.findAll().size();

        // Create the AdjustmentFactor with an existing ID
        adjustmentFactor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdjustmentFactorMockMvc.perform(post("/api/adjustment-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentFactor)))
            .andExpect(status().isBadRequest());

        // Validate the AdjustmentFactor in the database
        List<AdjustmentFactor> adjustmentFactorList = adjustmentFactorRepository.findAll();
        assertThat(adjustmentFactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdjustmentFactors() throws Exception {
        // Initialize the database
        adjustmentFactorRepository.saveAndFlush(adjustmentFactor);

        // Get all the adjustmentFactorList
        restAdjustmentFactorMockMvc.perform(get("/api/adjustment-factors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adjustmentFactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].conditionOperator").value(hasItem(DEFAULT_CONDITION_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getAdjustmentFactor() throws Exception {
        // Initialize the database
        adjustmentFactorRepository.saveAndFlush(adjustmentFactor);

        // Get the adjustmentFactor
        restAdjustmentFactorMockMvc.perform(get("/api/adjustment-factors/{id}", adjustmentFactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adjustmentFactor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION.toString()))
            .andExpect(jsonPath("$.conditionOperator").value(DEFAULT_CONDITION_OPERATOR.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdjustmentFactor() throws Exception {
        // Get the adjustmentFactor
        restAdjustmentFactorMockMvc.perform(get("/api/adjustment-factors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdjustmentFactor() throws Exception {
        // Initialize the database
        adjustmentFactorRepository.saveAndFlush(adjustmentFactor);

        int databaseSizeBeforeUpdate = adjustmentFactorRepository.findAll().size();

        // Update the adjustmentFactor
        AdjustmentFactor updatedAdjustmentFactor = adjustmentFactorRepository.findById(adjustmentFactor.getId()).get();
        // Disconnect from session so that the updates on updatedAdjustmentFactor are not directly saved in db
        em.detach(updatedAdjustmentFactor);
        updatedAdjustmentFactor
            .name(UPDATED_NAME)
            .condition(UPDATED_CONDITION)
            .conditionOperator(UPDATED_CONDITION_OPERATOR)
            .cost(UPDATED_COST);

        restAdjustmentFactorMockMvc.perform(put("/api/adjustment-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdjustmentFactor)))
            .andExpect(status().isOk());

        // Validate the AdjustmentFactor in the database
        List<AdjustmentFactor> adjustmentFactorList = adjustmentFactorRepository.findAll();
        assertThat(adjustmentFactorList).hasSize(databaseSizeBeforeUpdate);
        AdjustmentFactor testAdjustmentFactor = adjustmentFactorList.get(adjustmentFactorList.size() - 1);
        assertThat(testAdjustmentFactor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdjustmentFactor.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testAdjustmentFactor.getConditionOperator()).isEqualTo(UPDATED_CONDITION_OPERATOR);
        assertThat(testAdjustmentFactor.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingAdjustmentFactor() throws Exception {
        int databaseSizeBeforeUpdate = adjustmentFactorRepository.findAll().size();

        // Create the AdjustmentFactor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdjustmentFactorMockMvc.perform(put("/api/adjustment-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentFactor)))
            .andExpect(status().isBadRequest());

        // Validate the AdjustmentFactor in the database
        List<AdjustmentFactor> adjustmentFactorList = adjustmentFactorRepository.findAll();
        assertThat(adjustmentFactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdjustmentFactor() throws Exception {
        // Initialize the database
        adjustmentFactorRepository.saveAndFlush(adjustmentFactor);

        int databaseSizeBeforeDelete = adjustmentFactorRepository.findAll().size();

        // Delete the adjustmentFactor
        restAdjustmentFactorMockMvc.perform(delete("/api/adjustment-factors/{id}", adjustmentFactor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AdjustmentFactor> adjustmentFactorList = adjustmentFactorRepository.findAll();
        assertThat(adjustmentFactorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdjustmentFactor.class);
        AdjustmentFactor adjustmentFactor1 = new AdjustmentFactor();
        adjustmentFactor1.setId(1L);
        AdjustmentFactor adjustmentFactor2 = new AdjustmentFactor();
        adjustmentFactor2.setId(adjustmentFactor1.getId());
        assertThat(adjustmentFactor1).isEqualTo(adjustmentFactor2);
        adjustmentFactor2.setId(2L);
        assertThat(adjustmentFactor1).isNotEqualTo(adjustmentFactor2);
        adjustmentFactor1.setId(null);
        assertThat(adjustmentFactor1).isNotEqualTo(adjustmentFactor2);
    }
}
