package nbcc.resto.controller;

import nbcc.auth.service.LoginService;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.viewmodel.DiningTableListViewModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/diningTable"})
public class DiningTableController {

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
}
