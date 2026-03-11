package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.auth.service.LoginService;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.viewmodel.DiningTableListViewModel;
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
@RequestMapping({"/diningTable"})
public class DiningTableController {

    private static final Logger logger = LoggerFactory.getLogger(DiningTableController.class);
    private final DiningTableService diningTableService;
    private final LoginService loginService;

    public DiningTableController(DiningTableService diningTableService, LoginService loginService) {
        this.diningTableService = diningTableService;
        this.loginService = loginService;
    }

    @GetMapping
    public String getAll(Model model) {

        var result = diningTableService.getAll();

        DiningTableListViewModel viewModel =
                new DiningTableListViewModel(result.getValue(), loginService.isLoggedIn());

        model.addAttribute("viewModel", viewModel);

        return "diningTable/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("table", new DiningTable());
        return "diningTable/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("table") DiningTable table, BindingResult br) {

        var result = diningTableService.create(table);

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "diningTable/create";
        }

        return "redirect:/diningTable";
    }

    @GetMapping("/edit/{tableId}")
    public String edit(@PathVariable Long tableId, Model model) {

        var result = diningTableService.get(tableId);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to edit was not found");
            return "error/errorPage";
        }

        loadModel(result.getValue(), model, true);
        return "diningTable/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("diningTable") DiningTable table, BindingResult br, Model model) {

        var result = diningTableService.update(table);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            loadModel(table, model, true);
            return "diningTable/edit";
        }

        return "redirect:/diningTable";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {

        var result = diningTableService.get(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to delete was not found");
            return "error/errorPage";
        }

        loadModel(result.getValue(), model);

        return "diningTable/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        var result = diningTableService.delete(id);
        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/diningTable";
    }


    private void loadModel(DiningTable table, Model model) {
        loadModel(table, model, false);
    }

    private void loadModel(DiningTable table, Model model, boolean showManage) {

       model.addAttribute("table", table);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
