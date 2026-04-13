package nbcc.resto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Collection;

public class Menu {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @JsonIgnore
    private Boolean archived = false;

    private Long version;

    @JsonIgnoreProperties({"description", "version", "menu", "menuId"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<MenuItem> menuItems;

    public Menu() {
    }

    public Menu(Menu menu) {
        this(menu.getId(), menu.getName(), menu.getDescription(), menu.getVersion());
    }

    public Menu(Long id, String name, String description, Long version) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
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

    public Boolean getArchived() {
        return archived;
    }

    public Menu setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }
    public Collection<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Menu setMenuItems(Collection<MenuItem> menuItems) {
        this.menuItems = menuItems;
        return this;
    }
}
