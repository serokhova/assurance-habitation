package com.example.assurancehabitation.entity.assurance;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an insurance coverage (fire, theft, water damage...).
 */
@Entity
@Table(name = "coverage", schema = "assurance")
public class Coverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    // Lazy Load pattern
    @ManyToMany(mappedBy = "coverages", fetch = FetchType.LAZY)
    private List<Contract> contracts = new ArrayList<>();

    public Coverage() {}

    public Coverage(String code, String description, BigDecimal basePrice) {
        this.code = code;
        this.description = description;
        this.basePrice = basePrice;
    }

    public Coverage(String code, String description, Double basePrice) {
        this(code, description, basePrice != null ? BigDecimal.valueOf(basePrice) : null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public List<Contract> getContracts() { return contracts; }
    public void setContracts(List<Contract> contracts) { this.contracts = contracts; }
}
