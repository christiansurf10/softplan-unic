package com.softplan.transport.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Trajectory.
 */
@Entity
@Table(name = "trajectory")
public class Trajectory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("trajectories")
    private Vehicle vehicle;

    @ManyToMany
    @JoinTable(name = "trajectory_road_type",
               joinColumns = @JoinColumn(name = "trajectory_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "road_type_id", referencedColumnName = "id"))
    private Set<RoadType> roadTypes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "trajectory_adjustement_factor",
               joinColumns = @JoinColumn(name = "trajectory_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "adjustement_factor_id", referencedColumnName = "id"))
    private Set<AdjustmentFactor> adjustementFactors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Trajectory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Trajectory vehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<RoadType> getRoadTypes() {
        return roadTypes;
    }

    public Trajectory roadTypes(Set<RoadType> roadTypes) {
        this.roadTypes = roadTypes;
        return this;
    }

    public Trajectory addRoadType(RoadType roadType) {
        this.roadTypes.add(roadType);
        roadType.getTrajectories().add(this);
        return this;
    }

    public Trajectory removeRoadType(RoadType roadType) {
        this.roadTypes.remove(roadType);
        roadType.getTrajectories().remove(this);
        return this;
    }

    public void setRoadTypes(Set<RoadType> roadTypes) {
        this.roadTypes = roadTypes;
    }

    public Set<AdjustmentFactor> getAdjustementFactors() {
        return adjustementFactors;
    }

    public Trajectory adjustementFactors(Set<AdjustmentFactor> adjustmentFactors) {
        this.adjustementFactors = adjustmentFactors;
        return this;
    }

    public Trajectory addAdjustementFactor(AdjustmentFactor adjustmentFactor) {
        this.adjustementFactors.add(adjustmentFactor);
        adjustmentFactor.getTrajectories().add(this);
        return this;
    }

    public Trajectory removeAdjustementFactor(AdjustmentFactor adjustmentFactor) {
        this.adjustementFactors.remove(adjustmentFactor);
        adjustmentFactor.getTrajectories().remove(this);
        return this;
    }

    public void setAdjustementFactors(Set<AdjustmentFactor> adjustmentFactors) {
        this.adjustementFactors = adjustmentFactors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trajectory trajectory = (Trajectory) o;
        if (trajectory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trajectory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trajectory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
