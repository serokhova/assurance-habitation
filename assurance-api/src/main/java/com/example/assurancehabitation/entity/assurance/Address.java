package com.example.assurancehabitation.entity.assurance;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

/**
 * Embedded Value pattern : regroupe les champs d'adresse postale
 * dans un objet valeur réutilisable, stocké dans la table accommodation.
 */
@Embeddable
public class Address {

    @Column(nullable = false, length = 150)
    private String street;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 60)
    private String country;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    public Address() {}

    public Address(String street, String city, String country, String postalCode) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}
