package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.auth.service.LoginService;
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


    public SeatingController(LoginService loginService, SeatingService seatingService, EventService eventService, DiningTableService diningTableService) {
        this.loginService = loginService;
        this.seatingService = seatingService;
        this.eventService = eventService;
        this.diningTableService = diningTableService;
    }

        @GetMapping
        public String getAll(Model model) {

            var result = seatingService.getAll();

            SeatingViewModel viewModel =
                    new SeatingViewModel(result.getValue(), loginService.isLoggedIn());

            model.addAttribute("viewModel", viewModel);
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

            var result = seatingService.create(seating);

            if (result.isInvalid()) {
                addErrorsToBindingResults(br, result);
                model.addAttribute("events", eventService.getAll().getValue());
                model.addAttribute("tables", diningTableService.getAll().getValue());
                return "seating/create";
            }

            return "redirect:/seating";
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
            logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
            model.addAttribute("message", "Unexpected Error Occurred");
            return "error/errorPage";
        }

}
