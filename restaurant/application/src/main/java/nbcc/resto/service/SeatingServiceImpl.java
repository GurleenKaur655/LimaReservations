package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class SeatingServiceImpl implements SeatingService {

    private final Logger logger = LoggerFactory.getLogger(SeatingServiceImpl.class);

    private final SeatingRepository seatingRepository;
    private final SeatingValidationService  validationService;

    public SeatingServiceImpl(SeatingRepository seatingRepository, SeatingValidationService seatingValidationService) {
        this.seatingRepository = seatingRepository;
        this.validationService = seatingValidationService;
    }

    @Override
    public Result<Collection<Seating>> getAll() {
        try {
            return ValidationResults.success(seatingRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all seating", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> create(Seating seating) {
        try {
            var errors = validationService.validate(seating);

            if (errors.isEmpty()) {
                return ValidationResults.success(seatingRepository.create(seating));
            } else {
                logger.debug("Validation errors for seating create {}: {}", seating, errors);
                return ValidationResults.invalid(seating, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating seating {}", seating, e);
            return ValidationResults.error(seating);
        }
    }
}
