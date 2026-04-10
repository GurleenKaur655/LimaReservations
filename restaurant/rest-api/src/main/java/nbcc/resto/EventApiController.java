package nbcc.resto;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;
import nbcc.resto.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.result.ResultHandler.handleResult;

@Tag(name = "Events API")
@RestController
@RequestMapping("/api/event")
public class EventApiController {

    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Get all events")
    @GetMapping
    public ResponseEntity<Result<Collection<Event>>> getAll() {
        var result = eventService.getAllWithSeating();
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get event by id")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<Event>> get(@PathVariable Long id) {

        var result = eventService.getWithSeating(id);
        return handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
