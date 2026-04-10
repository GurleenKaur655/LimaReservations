package nbcc.resto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Menu;
import nbcc.resto.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.result.ResultHandler.handleResult;

@Tag(name = "Menu API")
@RestController
@RequestMapping("/api/menu")
public class MenuApiController {

    private final MenuService menuService;

    public MenuApiController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "Get all menu")
    @GetMapping
    public ResponseEntity<Result<Collection<Menu>>> getAll() {
        var result = menuService.getAll();
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get menu by id")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<Menu>> get(@PathVariable Long id) {

        var result = menuService.getWithMenuItems(id);
        return handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
