package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.auth.service.LoginService;
import nbcc.resto.dto.Event;
import nbcc.resto.service.EventService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodel.EventListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/event"})
public class EventController {
    private final LoginService loginService;

    private final EventService eventService;

    private final SeatingService seatingService;

    private final Logger logger = LoggerFactory.getLogger(EventController.class);

    public EventController(LoginService loginService, EventService eventService, SeatingService seatingService) {
        this.loginService = loginService;
        this.eventService = eventService;
        this.seatingService = seatingService;
    }


    @GetMapping
    public String getAll(Model model) {

        var result = eventService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving albums");
            return "error/errorPage";
        }

        EventListViewModel viewModel = new EventListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "event/list";
    }


    @GetMapping("/{eventId}")
    public String details(@PathVariable Long eventId, Model model) {

        var result = eventService.get(eventId);
        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The Event you are trying to view was not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());

        return "event/detail";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("event", new Event());
        return "event/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("event") Event album, BindingResult br) {

        var result = eventService.create(album);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "event/create";
        }

        return "redirect:/event";
    }
    @GetMapping("/edit/{eventId}")
    public String edit(@PathVariable Long eventId, Model model) {

        var result = eventService.get(eventId);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The event you are trying to edit was not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());
        return "event/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("event") Event event, BindingResult br, Model model) {

        var result = eventService.update(event);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("event", result.getValue());
            return "event/edit";
        }

        return "redirect:/event";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {

        var result = eventService.get(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The event you are trying to delete was not found");
            return "error/errorPage";
        }
        var seatingResult = seatingService.getAll(id);
        model.addAttribute("event", result.getValue());
        model.addAttribute("eventSeating", seatingResult.getValue());
        return "event/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        var result = eventService.delete(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/event";
    }
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Model model) {

        var result = eventService.search(name, startDate, endDate);

        EventListViewModel viewModel = new EventListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);

        return "event/list";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
