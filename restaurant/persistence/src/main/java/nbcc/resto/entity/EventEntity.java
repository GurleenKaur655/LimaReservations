package nbcc.resto.entity;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@EntityListeners(AuditingEntityListener.class)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Integer durationMinutes;

    @NotNull
    private BigDecimal price;


    private Boolean active;

    private Boolean archived = false;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public EventEntity() {

    }

    public Long getId() {
        return id;
    }

    public EventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public EventEntity setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public EventEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public EventEntity setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public EventEntity setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public EventEntity setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public EventEntity setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public EventEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public EventEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public EventEntity setVersion(Long version) {
        this.version = version;
        return this;
    }
}
