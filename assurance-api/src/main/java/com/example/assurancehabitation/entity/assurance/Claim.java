package com.example.assurancehabitation.entity.assurance;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité représentant un sinistre déclaré par un assuré.
 * Lazy Load pattern : toutes les relations ManyToOne sont chargées à la demande.
 */
@Entity
@Table(name = "claim", schema = "assurance")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Lazy Load pattern
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Lazy Load pattern
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    // Lazy Load pattern
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coverage_id", nullable = false)
    private Coverage coverage;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Claim() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDate getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }

    public Coverage getCoverage() { return coverage; }
    public void setCoverage(Coverage coverage) { this.coverage = coverage; }
}
