package com.example.demo_park_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "clients_spots")
@EntityListeners(AuditingEntityListener.class)
public class ClientSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "receipt_number", nullable = false, unique = true, length = 15)
    private String receipt;

    @Column(name = "plate", nullable = false, length = 8)
    private String plate;

    @Column(name = "brand", nullable = false, length = 45)
    private String brand;

    @Column(name = "model", nullable = false, length = 45)
    private String model;

    @Column(name = "color", nullable = false, length = 45)
    private String color;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "exit_date")
    private LocalDateTime exitDate;

    @Column(name = "total_value", columnDefinition = "double")
    private BigDecimal value;

    @Column(name = "discount", columnDefinition = "double")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_parking_spots", nullable = false)
    private ParkingSpots codeParkingSpots;

    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @org.springframework.data.annotation.CreatedBy
    @Column(name = "created_by")
    private String CreatedBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSpot that = (ClientSpot) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
