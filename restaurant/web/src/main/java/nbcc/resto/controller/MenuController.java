package nbcc.resto.controller;


import jakarta.servlet.http.HttpServletRequest;
import nbcc.auth.service.LoginService;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Menu;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.service.MenuService;
import nbcc.resto.viewmodel.EventListViewModel;
import nbcc.resto.viewmodel.MenuViewModel;
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
@RequestMapping({"/menu"})
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    private final MenuService menuService;
    private final LoginService loginService;

    public MenuController(MenuService menuService, LoginService loginService) {
        this.menuService = menuService;
        this.loginService = loginService;
    }

    @GetMapping
    public String getAll(Model model) {

        var result = menuService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving menus");
            return "error/errorPage";
        }

        MenuViewModel viewModel = new MenuViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "menu/list";
    }


    @GetMapping("/{menuId}")
    public String details(@PathVariable Long menuId, Model model) {

        var result = menuService.get(menuId);
        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to view was not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());

        return "menu/detail";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("menu", new Menu());
        return "menu/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("menu") Menu menu, BindingResult br) {

        var result = menuService.create(menu);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "menu/create";
        }

        return "redirect:/menu";
    }

    @GetMapping("/edit/{menuId}")
    public String edit(@PathVariable Long menuId, Model model) {

        var result = menuService.get(menuId);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to edit was not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());
        return "menu/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("menu") Menu menu, BindingResult br, Model model) {

        var result = menuService.update(menu);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("menu", result.getValue());
            return "menu/edit";
        }

        return "redirect:/menu";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {

        var result = menuService.get(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to delete was not found");
            return "error/errorPage";
        }
        model.addAttribute("menu", result.getValue());
        return "menu/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        var result = menuService.delete(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/menu";
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
