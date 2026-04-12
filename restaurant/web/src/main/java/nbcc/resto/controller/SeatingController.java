package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nbcc.auth.service.LoginService;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Seating;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.service.EventService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodel.SeatingViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/seating"})
public class SeatingController {

    private static final Logger logger = LoggerFactory.getLogger(SeatingController.class);

    private final LoginService loginService;
    private final SeatingService seatingService;
    private final EventService eventService;
    private final DiningTableService diningTableService;

    public SeatingController(LoginService loginService, SeatingService seatingService,
                             EventService eventService, DiningTableService diningTableService) {
        this.loginService = loginService;
        this.seatingService = seatingService;
        this.eventService = eventService;
        this.diningTableService = diningTableService;
    }

    @GetMapping
    public String getAll(Model model) {
        var result = seatingService.getAll();
        model.addAttribute("viewModel", new SeatingViewModel(result.getValue(), loginService.isLoggedIn()));
        return "seating/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("seating", new Seating());
        model.addAttribute("events", eventService.getAll().getValue());
        model.addAttribute("tables", diningTableService.getAll().getValue());
        return "seating/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("seating") Seating seating, BindingResult br, Model model) {

        resolveTableIds(seating);

        var result = seatingService.create(seating);

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("events", eventService.getAll().getValue());
            model.addAttribute("tables", diningTableService.getAll().getValue());
            return "seating/create";
        }

        return "redirect:/seating";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var result = seatingService.get(id);

        if (result.isError()) return "error/errorPage";

        if (result.isEmpty()) {
            model.addAttribute("message", "The seating you are trying to edit was not found");
            return "error/errorPage";
        }

        var seating = result.getValue();

        if (seating.getTables() != null) {
            seating.setTableIds(seating.getTables().stream()
                    .map(DiningTable::getId)
                    .toList());
        }

        model.addAttribute("seating", seating);
        model.addAttribute("events", eventService.getAll().getValue());
        model.addAttribute("tables", diningTableService.getAll().getValue());
        return "seating/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("seating") Seating seating, BindingResult br, Model model) {

        resolveTableIds(seating);

        var result = seatingService.update(seating);

        if (result.isError()) return "error/errorPage";

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("seating", result.getValue());
            model.addAttribute("events", eventService.getAll().getValue());
            model.addAttribute("tables", diningTableService.getAll().getValue());
            return "seating/edit";
        }

        return "redirect:/seating";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {
        var result = seatingService.get(id);

        if (result.isError()) return "error/errorPage";

        if (result.isEmpty()) {
            model.addAttribute("message", "The seating you are trying to delete was not found");
            return "error/errorPage";
        }

        model.addAttribute("seating", result.getValue());
        return "seating/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        var result = seatingService.delete(id);
        if (result.isError()) return "error/errorPage";
        return "redirect:/seating";
    }

    private void resolveTableIds(Seating seating) {
        if (seating.getTableIds() == null || seating.getTableIds().isEmpty()) return;

        List<DiningTable> tables = seating.getTableIds().stream()
                .map(id -> diningTableService.get(id).getValue()) 
                .filter(t -> t != null)
                .toList();

        seating.setTables(tables);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ",
                request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}