package com.softplan.transport.web.rest;

import com.softplan.transport.domain.Trajectory;
import com.softplan.transport.repository.AdjustmentFactorRepository;
import com.softplan.transport.repository.RoadTypeRepository;
import com.softplan.transport.repository.TrajectoryRepository;
import com.softplan.transport.service.TrajectoryService;
import com.softplan.transport.web.rest.errors.BadRequestAlertException;
import com.softplan.transport.web.rest.util.HeaderUtil;
import com.softplan.transport.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Trajectory.
 */
@PermitAll
@RestController
@RequestMapping("/api")
public class TrajectoryResource {

    private final Logger log = LoggerFactory.getLogger(TrajectoryResource.class);

    private static final String ENTITY_NAME = "trajectory";

    private final TrajectoryRepository trajectoryRepository;
    @Autowired
    private  TrajectoryService trajectoryService;
    @Autowired
    private RoadTypeRepository roadTypeRepository;
    @Autowired
    private AdjustmentFactorRepository adjustmentFactorRepository;

    public TrajectoryResource(TrajectoryRepository trajectoryRepository) {
        this.trajectoryRepository = trajectoryRepository;
    }

    /**
     * POST  /trajectories : Create a new trajectory.
     *
     * @param trajectory the trajectory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trajectory, or with status 400 (Bad Request) if the trajectory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trajectories")
    public ResponseEntity<Trajectory> createTrajectory(@RequestBody Trajectory trajectory) throws URISyntaxException {
        log.debug("REST request to save Trajectory : {}", trajectory);
        if (trajectory.getId() != null) {
            throw new BadRequestAlertException("A new trajectory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(roadTypeRepository != null)
        trajectory.setRoadTypes(roadTypeRepository.findAll().stream().collect(Collectors.toSet()));

        if(adjustmentFactorRepository != null)
        trajectory.setAdjustementFactors(adjustmentFactorRepository.findAll().stream().collect(Collectors.toSet()));

        if(trajectoryService!= null & trajectory != null)
        trajectory.setTotalCost(trajectoryService.calculateTotalCostTrajectory(trajectory));

        Trajectory result = trajectoryRepository.save(trajectory);
        return ResponseEntity.created(new URI("/api/trajectories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * PUT  /trajectories : Updates an existing trajectory.
     *
     * @param trajectory the trajectory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trajectory,
     * or with status 400 (Bad Request) if the trajectory is not valid,
     * or with status 500 (Internal Server Error) if the trajectory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trajectories")
    public ResponseEntity<Trajectory> updateTrajectory(@RequestBody Trajectory trajectory) throws URISyntaxException {
        log.debug("REST request to update Trajectory : {}", trajectory);
        if (trajectory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if(trajectoryService != null & trajectory.getRoadTypes() != null)
        trajectory.setTotalCost(trajectoryService.calculateTotalCostTrajectory(trajectory));

        Trajectory result = trajectoryRepository.save(trajectory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trajectory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trajectories : get all the trajectories.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of trajectories in body
     */
    @GetMapping("/trajectories")
    public ResponseEntity<List<Trajectory>> getAllTrajectories(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Trajectories");
        Page<Trajectory> page;
        if (eagerload) {
            page = trajectoryRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = trajectoryRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/trajectories?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /trajectories/:id : get the "id" trajectory.
     *
     * @param id the id of the trajectory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trajectory, or with status 404 (Not Found)
     */
    @GetMapping("/trajectories/{id}")
    public ResponseEntity<Trajectory> getTrajectory(@PathVariable Long id) {
        log.debug("REST request to get Trajectory : {}", id);
        Optional<Trajectory> trajectory = trajectoryRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(trajectory);
    }

    /**
     * DELETE  /trajectories/:id : delete the "id" trajectory.
     *
     * @param id the id of the trajectory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trajectories/{id}")
    public ResponseEntity<Void> deleteTrajectory(@PathVariable Long id) {
        log.debug("REST request to delete Trajectory : {}", id);
        trajectoryRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
