package nbcc.resto.service;

import jakarta.validation.Valid;
import jakarta.validation.Validation;
import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Event;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.validation.EventValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class EventServiceImpl implements EventService {
    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final EventValidationService validationService;
    private final MenuService menuService;

    public EventServiceImpl(EventRepository eventRepository, EventValidationService validationService, MenuService menuService) {
        this.eventRepository = eventRepository;
        this.validationService = validationService;
        this.menuService = menuService;
    }

    @Override
    public Result<Collection<Event>> getAll() {
        try {
            var events = eventRepository.getAll();
            return ValidationResults.success(events);
        } catch (Exception e) {
            logger.error("Error retrieving all event", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Event> get(Long id) {
        try {
            return ValidationResults.success(eventRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Event> create(Event event) {
        try {

            if (event.getMenu() != null && event.getMenu().getId() == null) {
                event.setMenu(null);
            }

            if (event.getMenu() != null && event.getMenu().getId() != null) {
                var menuResult = menuService.get(event.getMenu().getId());
                var menu = menuResult.getValue();
                event.setMenu(menu);
            }

            var errors = validationService.validate(event);

            if (errors.isEmpty()) {
                if (event.getActive() == true &&
                        (event.getMenu() == null || event.getMenu().getId() == null) ) {
                    event.setActive(false);
                }

                return ValidationResults.success(eventRepository.create(event));
            } else {
                // don't call the create method if there are validation errors
                logger.debug("Validation errors for event create {}: {}", event, errors);
                return ValidationResults.invalid(event, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating event {}", event, e);
            return ValidationResults.error(event);
        }
    }

    @Override
    public ValidatedResult<Event> update(Event event) {
        try {

            if (event.getMenu() != null && event.getMenu().getId() == null) {
                event.setMenu(null);
            }

            if (event.getMenu() != null && event.getMenu().getId() != null) {
                var menuResult = menuService.get(event.getMenu().getId());
                var menu = menuResult.getValue();
                event.setMenu(menu);
            }

            var errors = validationService.validate(event);

            if (errors.isEmpty()) {
                try {
                    if (event.getActive() == true &&
                            (event.getMenu() == null || event.getMenu().getId() == null) ) {
                        event.setActive(false);
                    }

                    return ValidationResults.success(eventRepository.update(event));
                } catch (ConcurrencyException e) {
                    errors.add(new ValidationError("Event was modified since it was displayed, please refresh and try again"));
                }
            }

            logger.debug("Validation errors for event update {}: {}", event, errors);
            return ValidationResults.invalid(event, errors);

        } catch (Exception e) {
            logger.error("Error updating event {}", event, e);
            return ValidationResults.error(event);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            var result = eventRepository.get(id);
            if (result.isPresent()) {
                var event = result.get();
                if (event.getEndDate() != null && event.getEndDate().isBefore(LocalDate.now())) {
                    eventRepository.archive(id);
                    logger.debug("Past event with id {} archived instead of deleted", id);
                } else {
                    eventRepository.delete(id);
                    logger.debug("Event with id {} deleted", id);
                }
            }
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> archive(Long id) {
       try{
           var result = eventRepository.get(id);
           if (result.isEmpty()) {
               return ValidationResults.invalid(null, "Event not found");
           }
           var event = result.get();
           if (event.getEndDate() == null || !event.getEndDate().isBefore(LocalDate.now())) {
               return ValidationResults.invalid(null, "Only past events can be archived");
           }

           eventRepository.archive(id);
           logger.debug("Event with id {} archived", id);
           return ValidationResults.success();
       }catch (Exception e) {
           logger.error("Error archiving event with id: {}", id, e);
           return ValidationResults.error(e);
       }
    }

    @Override
    public ValidatedResult<Collection<Event>> search(String name, LocalDate startDate, LocalDate endDate) {
       try {

           return ValidationResults.success(eventRepository.search(name, startDate, endDate));
       } catch (Exception e) {
           logger.error("Error searching for events", e);
           return ValidationResults.error(e);
       }
    }

}
