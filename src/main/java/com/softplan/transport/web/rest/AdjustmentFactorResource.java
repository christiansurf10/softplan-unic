package com.softplan.transport.web.rest;
import com.softplan.transport.domain.AdjustmentFactor;
import com.softplan.transport.repository.AdjustmentFactorRepository;
import com.softplan.transport.web.rest.errors.BadRequestAlertException;
import com.softplan.transport.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AdjustmentFactor.
 */
@RestController
@RequestMapping("/api")
public class AdjustmentFactorResource {

    private final Logger log = LoggerFactory.getLogger(AdjustmentFactorResource.class);

    private static final String ENTITY_NAME = "adjustmentFactor";

    private final AdjustmentFactorRepository adjustmentFactorRepository;

    public AdjustmentFactorResource(AdjustmentFactorRepository adjustmentFactorRepository) {
        this.adjustmentFactorRepository = adjustmentFactorRepository;
    }

    /**
     * POST  /adjustment-factors : Create a new adjustmentFactor.
     *
     * @param adjustmentFactor the adjustmentFactor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adjustmentFactor, or with status 400 (Bad Request) if the adjustmentFactor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/adjustment-factors")
    public ResponseEntity<AdjustmentFactor> createAdjustmentFactor(@RequestBody AdjustmentFactor adjustmentFactor) throws URISyntaxException {
        log.debug("REST request to save AdjustmentFactor : {}", adjustmentFactor);
        if (adjustmentFactor.getId() != null) {
            throw new BadRequestAlertException("A new adjustmentFactor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdjustmentFactor result = adjustmentFactorRepository.save(adjustmentFactor);
        return ResponseEntity.created(new URI("/api/adjustment-factors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adjustment-factors : Updates an existing adjustmentFactor.
     *
     * @param adjustmentFactor the adjustmentFactor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adjustmentFactor,
     * or with status 400 (Bad Request) if the adjustmentFactor is not valid,
     * or with status 500 (Internal Server Error) if the adjustmentFactor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/adjustment-factors")
    public ResponseEntity<AdjustmentFactor> updateAdjustmentFactor(@RequestBody AdjustmentFactor adjustmentFactor) throws URISyntaxException {
        log.debug("REST request to update AdjustmentFactor : {}", adjustmentFactor);
        if (adjustmentFactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdjustmentFactor result = adjustmentFactorRepository.save(adjustmentFactor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adjustmentFactor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adjustment-factors : get all the adjustmentFactors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of adjustmentFactors in body
     */
    @GetMapping("/adjustment-factors")
    public List<AdjustmentFactor> getAllAdjustmentFactors() {
        log.debug("REST request to get all AdjustmentFactors");
        return adjustmentFactorRepository.findAll();
    }

    /**
     * GET  /adjustment-factors/:id : get the "id" adjustmentFactor.
     *
     * @param id the id of the adjustmentFactor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adjustmentFactor, or with status 404 (Not Found)
     */
    @GetMapping("/adjustment-factors/{id}")
    public ResponseEntity<AdjustmentFactor> getAdjustmentFactor(@PathVariable Long id) {
        log.debug("REST request to get AdjustmentFactor : {}", id);
        Optional<AdjustmentFactor> adjustmentFactor = adjustmentFactorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(adjustmentFactor);
    }

    /**
     * DELETE  /adjustment-factors/:id : delete the "id" adjustmentFactor.
     *
     * @param id the id of the adjustmentFactor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/adjustment-factors/{id}")
    public ResponseEntity<Void> deleteAdjustmentFactor(@PathVariable Long id) {
        log.debug("REST request to delete AdjustmentFactor : {}", id);
        adjustmentFactorRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
