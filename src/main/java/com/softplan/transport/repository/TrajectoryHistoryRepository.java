package com.softplan.transport.repository;

import com.softplan.transport.domain.TrajectoryHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TrajectoryHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrajectoryHistoryRepository extends JpaRepository<TrajectoryHistory, Long> {

}
