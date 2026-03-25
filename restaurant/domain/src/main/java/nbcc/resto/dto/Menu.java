package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class Menu {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private Long version;

    public Menu() {
    }

    public Menu(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Menu setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Menu setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Menu setVersion(Long version) {
        this.version = version;
        return this;
    }
}
