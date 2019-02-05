package com.softplan.transport.web.rest;
import com.softplan.transport.domain.RoadType;
import com.softplan.transport.repository.RoadTypeRepository;
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
 * REST controller for managing RoadType.
 */
@RestController
@RequestMapping("/api")
public class RoadTypeResource {

    private final Logger log = LoggerFactory.getLogger(RoadTypeResource.class);

    private static final String ENTITY_NAME = "roadType";

    private final RoadTypeRepository roadTypeRepository;

    public RoadTypeResource(RoadTypeRepository roadTypeRepository) {
        this.roadTypeRepository = roadTypeRepository;
    }

    /**
     * POST  /road-types : Create a new roadType.
     *
     * @param roadType the roadType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roadType, or with status 400 (Bad Request) if the roadType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/road-types")
    public ResponseEntity<RoadType> createRoadType(@RequestBody RoadType roadType) throws URISyntaxException {
        log.debug("REST request to save RoadType : {}", roadType);
        if (roadType.getId() != null) {
            throw new BadRequestAlertException("A new roadType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoadType result = roadTypeRepository.save(roadType);
        return ResponseEntity.created(new URI("/api/road-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /road-types : Updates an existing roadType.
     *
     * @param roadType the roadType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roadType,
     * or with status 400 (Bad Request) if the roadType is not valid,
     * or with status 500 (Internal Server Error) if the roadType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/road-types")
    public ResponseEntity<RoadType> updateRoadType(@RequestBody RoadType roadType) throws URISyntaxException {
        log.debug("REST request to update RoadType : {}", roadType);
        if (roadType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoadType result = roadTypeRepository.save(roadType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roadType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /road-types : get all the roadTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roadTypes in body
     */
    @GetMapping("/road-types")
    public List<RoadType> getAllRoadTypes() {
        log.debug("REST request to get all RoadTypes");
        return roadTypeRepository.findAll();
    }

    /**
     * GET  /road-types/:id : get the "id" roadType.
     *
     * @param id the id of the roadType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roadType, or with status 404 (Not Found)
     */
    @GetMapping("/road-types/{id}")
    public ResponseEntity<RoadType> getRoadType(@PathVariable Long id) {
        log.debug("REST request to get RoadType : {}", id);
        Optional<RoadType> roadType = roadTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(roadType);
    }

    /**
     * DELETE  /road-types/:id : delete the "id" roadType.
     *
     * @param id the id of the roadType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/road-types/{id}")
    public ResponseEntity<Void> deleteRoadType(@PathVariable Long id) {
        log.debug("REST request to delete RoadType : {}", id);
        roadTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
