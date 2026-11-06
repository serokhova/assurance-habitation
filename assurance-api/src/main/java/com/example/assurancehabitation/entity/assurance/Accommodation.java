package com.example.assurancehabitation.entity.assurance;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un logement assuré.
 * Embedded Value pattern : l'adresse est encapsulée dans un @Embeddable.
 * Lazy Load pattern : la relation vers User est chargée à la demande.
 */
@Entity
@Table(name = "accommodation", schema = "assurance")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Embedded Value pattern : tous les champs d'adresse dans un objet valeur
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccommodationType type;

    @Column(name = "surface_area", nullable = false)
    private Integer surfaceArea;

    @Column(name = "room_count", nullable = false)
    private Integer roomCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Lazy Load pattern : User chargé uniquement si accédé
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Lazy Load pattern
    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    private List<Contract> contracts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Accommodation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public AccommodationType getType() { return type; }
    public void setType(AccommodationType type) { this.type = type; }

    public Integer getSurfaceArea() { return surfaceArea; }
    public void setSurfaceArea(Integer surfaceArea) { this.surfaceArea = surfaceArea; }

    public Integer getRoomCount() { return roomCount; }
    public void setRoomCount(Integer roomCount) { this.roomCount = roomCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Contract> getContracts() { return contracts; }
    public void setContracts(List<Contract> contracts) { this.contracts = contracts; }
}
