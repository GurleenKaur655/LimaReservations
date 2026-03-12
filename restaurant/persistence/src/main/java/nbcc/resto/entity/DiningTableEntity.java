package nbcc.resto.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="diningtable")
@EntityListeners(AuditingEntityListener.class)
public class DiningTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private Integer capacity;


    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public DiningTableEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Long getId() {
        return id;
    }

    public DiningTableEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiningTableEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public DiningTableEntity setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public DiningTableEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
