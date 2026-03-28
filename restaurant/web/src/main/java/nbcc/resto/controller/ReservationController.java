package nbcc.resto.controller;

import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.service.EventService;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodel.ReservationViewModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final EventService eventService;
    private final SeatingService seatingService;
    private final MenuItemService menuItemService;

    public ReservationController(ReservationService reservationService,
                                 EventService eventService,
                                 SeatingService seatingService,
                                 MenuItemService menuItemService) {
        this.reservationService = reservationService;
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.menuItemService = menuItemService;
    }

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {

        var events = eventService.getAll().getValue();
        var seatings = seatingService.getAll().getValue();

        var seatingsByEventId = seatings == null
                ? Collections.emptyMap()
                : seatings.stream()
                .filter(seating -> seating.getEvent() != null && seating.getEvent().getId() != null)
                .collect(Collectors.groupingBy(seating -> seating.getEvent().getId()));
        //was confused how to present it, took the help of web to understand grouping

        var menuItemsByMenuId = events == null ? Collections.emptyMap()
                : events.stream()
                .filter(e -> e.getMenu() != null && e.getMenu().getId() != null)
                .collect(Collectors.toMap(
                        e -> e.getMenu().getId(),
                        e -> menuItemService.getAll(e.getMenu().getId()).getValue()
                ));

        model.addAttribute("events", events);
        model.addAttribute("seatingsByEventId", seatingsByEventId);
        model.addAttribute("menuItemsByMenuId", menuItemsByMenuId);

        return "reservation/index";
    }

    @GetMapping("/create/{seatingId}")
    public String create(@PathVariable Long seatingId, Model model) {
        var result = seatingService.get(seatingId);
        if (result.isError()) {
            return "redirect:/reservation/index";
        }
        var seating = result.getValue();
        model.addAttribute("seating", seating);
        model.addAttribute("event", seating.getEvent());
        model.addAttribute("reservation", new Reservation().setSeating(seating));
        return "reservation/create";
    }

    @PostMapping("/create/{seatingId}")
    public String create(@PathVariable Long seatingId,
                        @ModelAttribute Reservation reservation,
                        BindingResult br,
                        Model model) {
        var result = seatingService.get(seatingId);
        if (result.isError()) {
            return "redirect:/reservation/index";
        }
        reservation.setSeating(result.getValue());

        var reservationResult = reservationService.create(reservation);

        if (reservationResult.isInvalid()) {
            addErrorsToBindingResults(br, reservationResult);
            model.addAttribute("reservation", reservation);
            model.addAttribute("seating", result.getValue());
            model.addAttribute("event", result.getValue().getEvent());
            return "reservation/create";
        }
        if (reservationResult.isError()) {
            return "error/errorPage";
        }

        return "redirect:/reservation/track/" + reservationResult.getValue().getUuid();
    }

    @GetMapping("/track")
    public String trackSearch(@RequestParam String uuid) {
        if (uuid == null || uuid.isBlank()) {
            return "redirect:/reservation";
        }
        try {
            return "redirect:/reservation/track/" + UUID.fromString(uuid);
        } catch (Exception e) {
            return "error/errorPage";
        }
    }

    @GetMapping("/track/{uuid}")
    public String track(@PathVariable UUID uuid, Model model) {
        var result = reservationService.getByUuid(uuid);
        if (result.isError() || result.isEmpty()) {
            return "redirect:/reservation";
        }
        model.addAttribute("reservation", result.getValue());
        return "reservation/track";
    }

    @GetMapping("/manage")
    @PreAuthorize("isAuthenticated()")
    public String manage(
                         @RequestParam(required = false) String eventName,
                         @RequestParam(required = false) ReservationStatus status,
                         Model model) {
        var reservations = reservationService.getAll(
                (eventName == null || eventName.isBlank()) ? null : eventName,
                status
        ).getValue();
        var events = eventService.getAll().getValue();
        model.addAttribute("statuses", ReservationStatus.values());
        model.addAttribute("viewModel", new ReservationViewModel(reservations, events, status, eventName));
        return "reservation/list";
    }

    @GetMapping("/manage/{id}")
    @PreAuthorize("isAuthenticated()")
    public String details(@PathVariable Long id, Model model) {
        var result = reservationService.get(id);
        if (result.isError()) return "error/errorPage";
        var reservation = result.getValue();
        model.addAttribute("reservation", reservation);
        model.addAttribute("event", reservation.getSeating().getEvent());
        return "reservation/detail";
    }

    @PostMapping("/manage/{id}/approve")
    @PreAuthorize("isAuthenticated()")
    public String approve(@PathVariable Long id,
                          @ModelAttribute("reservation") Reservation reservation,
                          BindingResult br,
                          Model model) {

        var result = reservationService.approve(id);

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "reservation");

            var reservationResult = result.getValue();
            model.addAttribute("reservation", reservationResult);
            model.addAttribute("event", reservationResult.getSeating().getEvent());
            model.addAttribute("actionError", result.getValidationErrors().iterator().next().getMessage());

            return "reservation/detail";
        }

        if (result.isError()) return "error/errorPage";

        return "redirect:/reservation/manage/" + id;
    }

    @PostMapping("/manage/{id}/deny")
    @PreAuthorize("isAuthenticated()")
    public String deny(@PathVariable Long id,
                       @ModelAttribute("reservation") Reservation reservation,
                       BindingResult br,
                       Model model) {

        var result = reservationService.deny(id);

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "reservation");

            var reservationResult = result.getValue();
            model.addAttribute("reservation", reservationResult);
            model.addAttribute("event", reservationResult.getSeating().getEvent());
            model.addAttribute("actionError", result.getValidationErrors().iterator().next().getMessage());

            return "reservation/detail";
        }

        if (result.isError()) return "error/errorPage";

        return "redirect:/reservation/manage/" + id;
    }
}
