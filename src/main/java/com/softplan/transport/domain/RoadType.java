package com.softplan.transport.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RoadType.
 */
@Entity
@Table(name = "road_type")
public class RoadType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_cost")
    private Double cost;

    @Column(name = "unit")
    private String unit;

    @ManyToMany(mappedBy = "roadTypes")
    @JsonIgnore
    private Set<Trajectory> trajectories = new HashSet<>();

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

    public RoadType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public RoadType cost(Double cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getUnit() {
        return unit;
    }

    public RoadType unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Set<Trajectory> getTrajectories() {
        return trajectories;
    }

    public RoadType trajectories(Set<Trajectory> trajectories) {
        this.trajectories = trajectories;
        return this;
    }

    public RoadType addTrajectory(Trajectory trajectory) {
        this.trajectories.add(trajectory);
        trajectory.getRoadTypes().add(this);
        return this;
    }

    public RoadType removeTrajectory(Trajectory trajectory) {
        this.trajectories.remove(trajectory);
        trajectory.getRoadTypes().remove(this);
        return this;
    }

    public void setTrajectories(Set<Trajectory> trajectories) {
        this.trajectories = trajectories;
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
        RoadType roadType = (RoadType) o;
        if (roadType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roadType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoadType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cost=" + getCost() +
            ", unit='" + getUnit() + "'" +
            "}";
    }
}
