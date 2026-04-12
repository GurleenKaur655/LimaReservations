package nbcc.resto.service;

import jakarta.transaction.Transactional;
import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.repository.DiningTableRepository;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.DiningTableValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class DiningTableServiceImpl implements  DiningTableService {

    private final Logger logger = LoggerFactory.getLogger(DiningTableServiceImpl.class);

    private final DiningTableRepository diningTableRepository;
    private final DiningTableValidationService validationService;
    private final SeatingRepository seatingRepository;
    private final ReservationRepository reservationRepository;

    public DiningTableServiceImpl(DiningTableRepository diningTableRepository, DiningTableValidationService validationService, SeatingRepository seatingRepository, ReservationRepository reservationRepository) {
        this.diningTableRepository = diningTableRepository;
        this.validationService = validationService;
        this.seatingRepository = seatingRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Result<Collection<DiningTable>> getAll() {
        try {
            return ValidationResults.success(diningTableRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all dining tables", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<DiningTable> get(Long id) {
        try {
            return ValidationResults.success(diningTableRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }


    @Override
    public ValidatedResult<DiningTable> create(DiningTable table) {
        try {

            var errors = validationService.validate(table);

            if (errors.isEmpty()) {

                table = diningTableRepository.create(table);

                if (table.getName() == null || table.getName().isBlank()) {
                    table.setName("Table " + table.getId());
                    table = diningTableRepository.update(table);
                }

                return ValidationResults.success(table);

            } else {

                logger.debug("Validation errors for table create {}: {}", table, errors);
                return ValidationResults.invalid(table, errors);

            }

        } catch (Exception e) {
            logger.error("Error creating table {}", table, e);
            return ValidationResults.error(table);
        }
    }

    @Override
    public ValidatedResult<DiningTable> update(DiningTable table) {
        try {
            var errors = validationService.validate(table);

            if (errors.isEmpty()) {
                if (table.getName() == null || table.getName().isBlank()) {
                    table.setName("Table " + table.getId());
                }

                try {
                    return ValidationResults.success(diningTableRepository.update(table));
                } catch (ConcurrencyException e) {
                    errors.add(new ValidationError("Dining table was modified since it was displayed, please refresh and try again"));
                }
            }

            logger.debug("Validation errors for table update {}: {}", table, errors);
            return ValidationResults.invalid(table, errors);

        } catch (Exception e) {
            logger.error("Error updating table {}", table, e);
            return ValidationResults.error(table);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            var seatings = seatingRepository.getByTableId(id);
            for (var seating : seatings) {
                reservationRepository.deleteBySeatingId(seating.getId());
            }
            seatingRepository.deleteByTableId(id);
            diningTableRepository.delete(id);
            logger.debug("Dining table with id {} deleted", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting dining table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
