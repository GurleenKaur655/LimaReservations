package nbcc.resto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DiningTable {

    private Long id;

    private String name;


    @Positive(message = "Capacity must be greater than 0")
    @NotNull(message = "Capacity cannot be blank")
    private Integer capacity;

    private Long version;

    public Long getId() {
        return id;
    }

    public DiningTable setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiningTable setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public DiningTable setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public DiningTable setVersion(Long version) {
        this.version = version;
        return this;
    }
}
