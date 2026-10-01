package nbcc.common.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.viewmodel.NavViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String authorizationDeniedExceptionExceptionHandler(Model model, AuthorizationDeniedException ex, HttpServletRequest request){
        logger.warn("Unauthorized on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        throw ex; // re throw to let the spring security framework handle it, but catch it so it doesn't get caught by the catch all error handler.
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandler(Model model, HttpServletRequest request) {
        logger.warn("Not found: {} {}", request.getMethod(), request.getRequestURI());
        addNav(model);
        return "error/404";
    }

    private void addNav(Model model) {
        NavViewModel nav;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean loggedIn = auth != null
                    && auth.isAuthenticated()
                    && !(auth instanceof AnonymousAuthenticationToken);
            nav = new NavViewModel(loggedIn, loggedIn ? auth.getName() : null);
        } catch (Exception e) {
            nav = new NavViewModel(false, null);   // fall back to the logged-out nav
        }
        model.addAttribute("navViewModel", nav);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
