package com.softplan.transport.repository;

import com.softplan.transport.domain.RoadType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoadType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoadTypeRepository extends JpaRepository<RoadType, Long> {

}
