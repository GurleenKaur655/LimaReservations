package nbcc.resto.repository;

import nbcc.resto.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuJPARepository extends JpaRepository<MenuEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query("""
            SELECT m FROM MenuEntity m
            WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    List<MenuEntity> search(@Param("name") String name);
    List<MenuEntity> findByArchivedFalse();
}
