package com.softplan.transport.repository;

import com.softplan.transport.domain.AdjustmentFactor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AdjustmentFactor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdjustmentFactorRepository extends JpaRepository<AdjustmentFactor, Long> {

}
