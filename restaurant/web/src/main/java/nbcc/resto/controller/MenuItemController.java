package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/menu"})
public class MenuItemController {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;
    private final MenuService menuService;


    public MenuItemController(MenuItemService menuItemService, MenuService menuService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
    }


    @GetMapping("/{menuId}/menuItem/create")
    public String create(@PathVariable Long menuId, Model model) {

        logger.debug("attempting to load new menu item for creation with menuId: {}", menuId);

        var menuResult = menuService.get(menuId);

        if (menuResult.isError()) {
            return "error/errorPage";
        }

        if (menuResult.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to add a menu item to was not found");
            return "error/errorPage";
        }

        var menu = menuResult.getValue();
        model.addAttribute("menu", menu);
        model.addAttribute("menuItem", new MenuItem(menu));

        return "menuItem/create";
    }

    @PostMapping("/{menuId}/menuItem/create")
    public String create(@PathVariable Long menuId,
                         @ModelAttribute("menuItem") MenuItem menuItem,
                         BindingResult br,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        logger.debug("attempting to create new menu item on menu with menuId: {}", menuId);

        var menuResult = menuService.get(menuId);
        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message",
                    "There was a problem trying to retrieve menu you are trying to add a menu item to");
            return "error/errorPage";
        }

        menuItem.setMenu(menuResult.getValue());
        var menuItemResult = menuItemService.create(menuItem);

        if(menuItemResult.isError()){
            model.addAttribute("message", "There was a problem trying to create the menu item");
            return "error/errorPage";
        }

        if(menuItemResult.isInvalid()){
            addErrorsToBindingResults(br, menuItemResult);
            model.addAttribute("menu", menuResult.getValue());
            return "menuItem/create";
        }

        redirectAttributes.addAttribute("menuId", menuId);
        return "redirect:/menu/edit/{menuId}";
    }

    @GetMapping("/{menuId}/menuItem/edit/{menuItemId}")
    public String edit(@PathVariable Long menuId, @PathVariable Long menuItemId, Model model) {

        logger.debug("loading menu item for edit with menuItemId: {}, and menuId: {}", menuItemId, menuId);

        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError() || menuItemResult.isEmpty()) {
            model.addAttribute("message",
                    "There was an error trying to retrieve the menu item you are trying to edit");
            return "error/errorPage";
        }

        var menuItem = menuItemResult.getValue();
        loadModel(menuItem, model);

        return "menuItem/edit";
    }

    @PostMapping("/{menuId}/menuItem/edit")
    public String edit(@PathVariable Long menuId,
                       @ModelAttribute("menuItem") MenuItem menuItem,
                       BindingResult br,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        logger.debug("attempting to edit menu item: {}, which belongs to menuId: {}", menuItem.getId(), menuId);

        var result = menuItemService.update(menuItem);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            var menuResult = menuService.get(menuItem.getMenuId());
            if (menuResult.hasValue()) {
                menuItem.setMenu(menuResult.getValue());
                loadModel(menuItem, model);
            }
            return "menuItem/edit";
        }

        redirectAttributes.addAttribute("menuId", menuItem.getMenuId());
        return "redirect:/menu/edit/{menuId}";
    }


    @GetMapping("{menuId}/menuItem/delete/{menuItemId}")
    public String delete(@PathVariable Long menuId, @PathVariable Long menuItemId, Model model) {

        logger.debug("loading menu item for delete with menuItemId: {}, and menuId: {}", menuItemId, menuId);

        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError()) {
            return "error/errorPage";
        }

        if (menuItemResult.isEmpty()) {
            model.addAttribute("message", "The menu item you are trying to delete was not found");
            return "error/errorPage";
        }

        var menuItem = menuItemResult.getValue();
        loadModel(menuItem, model);

        return "menuItem/delete";
    }

    @PostMapping("{menuId}/menuItem/delete/{menuItemId}")
    public String delete(@PathVariable Long menuId, @PathVariable Long menuItemId, RedirectAttributes redirectAttributes) {

        logger.debug("attempting to delete menu item: {}, which belongs to menuId: {}", menuItemId, menuId);

        var result = menuItemService.delete(menuItemId);
        if (result.isError()) {
            return "error/errorPage";
        }

        redirectAttributes.addAttribute("menuId", menuId);
        return "redirect:/menu/edit/{menuId}";
    }



    private void loadModel(MenuItem menuItem, Model model) {
        loadModel(menuItem, menuItem.getMenu(), model);
    }

    private void loadModel(MenuItem menuItem, Menu menu, Model model) {
        model.addAttribute("menuItem", menuItem);
        model.addAttribute("menu", menu);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
