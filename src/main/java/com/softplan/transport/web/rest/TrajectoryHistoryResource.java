package com.softplan.transport.web.rest;
import com.softplan.transport.domain.TrajectoryHistory;
import com.softplan.transport.repository.TrajectoryHistoryRepository;
import com.softplan.transport.web.rest.errors.BadRequestAlertException;
import com.softplan.transport.web.rest.util.HeaderUtil;
import com.softplan.transport.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TrajectoryHistory.
 */
@RestController
@RequestMapping("/api")
public class TrajectoryHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TrajectoryHistoryResource.class);

    private static final String ENTITY_NAME = "trajectoryHistory";

    private final TrajectoryHistoryRepository trajectoryHistoryRepository;

    public TrajectoryHistoryResource(TrajectoryHistoryRepository trajectoryHistoryRepository) {
        this.trajectoryHistoryRepository = trajectoryHistoryRepository;
    }

    /**
     * POST  /trajectory-histories : Create a new trajectoryHistory.
     *
     * @param trajectoryHistory the trajectoryHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trajectoryHistory, or with status 400 (Bad Request) if the trajectoryHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trajectory-histories")
    public ResponseEntity<TrajectoryHistory> createTrajectoryHistory(@RequestBody TrajectoryHistory trajectoryHistory) throws URISyntaxException {
        log.debug("REST request to save TrajectoryHistory : {}", trajectoryHistory);
        if (trajectoryHistory.getId() != null) {
            throw new BadRequestAlertException("A new trajectoryHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrajectoryHistory result = trajectoryHistoryRepository.save(trajectoryHistory);
        return ResponseEntity.created(new URI("/api/trajectory-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trajectory-histories : Updates an existing trajectoryHistory.
     *
     * @param trajectoryHistory the trajectoryHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trajectoryHistory,
     * or with status 400 (Bad Request) if the trajectoryHistory is not valid,
     * or with status 500 (Internal Server Error) if the trajectoryHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trajectory-histories")
    public ResponseEntity<TrajectoryHistory> updateTrajectoryHistory(@RequestBody TrajectoryHistory trajectoryHistory) throws URISyntaxException {
        log.debug("REST request to update TrajectoryHistory : {}", trajectoryHistory);
        if (trajectoryHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrajectoryHistory result = trajectoryHistoryRepository.save(trajectoryHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trajectoryHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trajectory-histories : get all the trajectoryHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trajectoryHistories in body
     */
    @GetMapping("/trajectory-histories")
    public ResponseEntity<List<TrajectoryHistory>> getAllTrajectoryHistories(Pageable pageable) {
        log.debug("REST request to get a page of TrajectoryHistories");
        Page<TrajectoryHistory> page = trajectoryHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trajectory-histories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /trajectory-histories/:id : get the "id" trajectoryHistory.
     *
     * @param id the id of the trajectoryHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trajectoryHistory, or with status 404 (Not Found)
     */
    @GetMapping("/trajectory-histories/{id}")
    public ResponseEntity<TrajectoryHistory> getTrajectoryHistory(@PathVariable Long id) {
        log.debug("REST request to get TrajectoryHistory : {}", id);
        Optional<TrajectoryHistory> trajectoryHistory = trajectoryHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trajectoryHistory);
    }

    /**
     * DELETE  /trajectory-histories/:id : delete the "id" trajectoryHistory.
     *
     * @param id the id of the trajectoryHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trajectory-histories/{id}")
    public ResponseEntity<Void> deleteTrajectoryHistory(@PathVariable Long id) {
        log.debug("REST request to delete TrajectoryHistory : {}", id);
        trajectoryHistoryRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
