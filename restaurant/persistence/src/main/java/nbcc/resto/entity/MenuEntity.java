package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="menu")
@EntityListeners(AuditingEntityListener.class)
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean archived = false;

    @Version
    private Long version;

    public MenuEntity() {
    }

    public Long getId() {
        return id;
    }

    public MenuEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MenuEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public MenuEntity setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuEntity setVersion(Long version) {
        this.version = version;
        return this;
    }
}
