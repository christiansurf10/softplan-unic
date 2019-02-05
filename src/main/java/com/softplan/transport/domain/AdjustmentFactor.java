package com.softplan.transport.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AdjustmentFactor.
 */
@Entity
@Table(name = "adjustment_factor")
public class AdjustmentFactor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_condition")
    private String condition;

    @Column(name = "condition_operator")
    private String conditionOperator;

    @Column(name = "jhi_cost")
    private Double cost;

    @ManyToMany(mappedBy = "adjustementFactors")
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

    public AdjustmentFactor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public AdjustmentFactor condition(String condition) {
        this.condition = condition;
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionOperator() {
        return conditionOperator;
    }

    public AdjustmentFactor conditionOperator(String conditionOperator) {
        this.conditionOperator = conditionOperator;
        return this;
    }

    public void setConditionOperator(String conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public Double getCost() {
        return cost;
    }

    public AdjustmentFactor cost(Double cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Set<Trajectory> getTrajectories() {
        return trajectories;
    }

    public AdjustmentFactor trajectories(Set<Trajectory> trajectories) {
        this.trajectories = trajectories;
        return this;
    }

    public AdjustmentFactor addTrajectory(Trajectory trajectory) {
        this.trajectories.add(trajectory);
        trajectory.getAdjustementFactors().add(this);
        return this;
    }

    public AdjustmentFactor removeTrajectory(Trajectory trajectory) {
        this.trajectories.remove(trajectory);
        trajectory.getAdjustementFactors().remove(this);
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
        AdjustmentFactor adjustmentFactor = (AdjustmentFactor) o;
        if (adjustmentFactor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adjustmentFactor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdjustmentFactor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", condition='" + getCondition() + "'" +
            ", conditionOperator='" + getConditionOperator() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
