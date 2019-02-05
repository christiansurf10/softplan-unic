package com.softplan.transport.repository;

import com.softplan.transport.domain.Trajectory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Trajectory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrajectoryRepository extends JpaRepository<Trajectory, Long> {

    @Query(value = "select distinct trajectory from Trajectory trajectory left join fetch trajectory.roadTypes left join fetch trajectory.adjustementFactors",
        countQuery = "select count(distinct trajectory) from Trajectory trajectory")
    Page<Trajectory> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct trajectory from Trajectory trajectory left join fetch trajectory.roadTypes left join fetch trajectory.adjustementFactors")
    List<Trajectory> findAllWithEagerRelationships();

    @Query("select trajectory from Trajectory trajectory left join fetch trajectory.roadTypes left join fetch trajectory.adjustementFactors where trajectory.id =:id")
    Optional<Trajectory> findOneWithEagerRelationships(@Param("id") Long id);

}
