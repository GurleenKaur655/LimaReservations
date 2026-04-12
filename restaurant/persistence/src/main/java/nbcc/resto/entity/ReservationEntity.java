package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import nbcc.resto.dto.ReservationStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation")
@EntityListeners(AuditingEntityListener.class)
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;

    public String getEmail() {
        return email;
    }

    public ReservationEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private Integer groupSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;


    @ManyToOne
    @JoinColumn(name = "seating_id", foreignKey = @ForeignKey(name = "fk_reservation_seating"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SeatingEntity seating;

    @Version
    private Long version;

    public UUID getUuid() {
        return uuid;
    }

    public ReservationEntity setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }



    public ReservationEntity() {

    }

    public Long getId() {
        return id;
    }

    public ReservationEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public ReservationEntity setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public ReservationEntity setStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ReservationEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public SeatingEntity getSeating() {
        return seating;
    }

    public ReservationEntity setSeating(SeatingEntity seating) {
        this.seating = seating;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public ReservationEntity setVersion(Long version) {
        this.version = version;
        return this;
    }
}
