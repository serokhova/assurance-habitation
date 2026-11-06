package com.example.assurancehabitation.entity.assurance;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un contrat d'assurance habitation.
 * Lazy Load pattern : relations chargées à la demande.
 */
@Entity
@Table(name = "contract", schema = "assurance")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractFormula formula;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Lazy Load pattern
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    // Lazy Load pattern : ManyToMany via contract_coverage
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "contract_coverage",
        schema = "assurance",
        joinColumns = @JoinColumn(name = "contract_id"),
        inverseJoinColumns = @JoinColumn(name = "coverage_id")
    )
    private List<Coverage> coverages = new ArrayList<>();

    // Lazy Load pattern
    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
    private List<Claim> claims = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Contract() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public ContractFormula getFormula() { return formula; }
    public void setFormula(ContractFormula formula) { this.formula = formula; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Accommodation getAccommodation() { return accommodation; }
    public void setAccommodation(Accommodation accommodation) { this.accommodation = accommodation; }

    public List<Coverage> getCoverages() { return coverages; }
    public void setCoverages(List<Coverage> coverages) { this.coverages = coverages; }

    public List<Claim> getClaims() { return claims; }
    public void setClaims(List<Claim> claims) { this.claims = claims; }
}
