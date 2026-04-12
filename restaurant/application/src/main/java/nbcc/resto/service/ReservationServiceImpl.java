package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.email.domain.EmailRequest;
import nbcc.email.service.EmailService;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.validation.ReservationValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final ReservationValidationService reservationValidationService;
    private final EmailService emailService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservationValidationService reservationValidationService,
                                  EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.reservationValidationService = reservationValidationService;
        this.emailService = emailService;
    }

    @Override
    public Result<Collection<Reservation>> getAll() {
        try {
            return ValidationResults.success(reservationRepository.getAll());
        } catch (Exception e) {
            logger.error("Error getting all reservations", e);
            return ValidationResults.error();
        }
    }

    @Override
    public ValidatedResult<Reservation> get(Long id) {
        try {
            return ValidationResults.success(reservationRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Reservation> create(Reservation reservation) {
        try {
            reservation.setStatus(ReservationStatus.PENDING);
            reservation.setUuid(UUID.randomUUID());

            var errors = reservationValidationService.validate(reservation);

            if (errors.isEmpty()) {
                var created = reservationRepository.create(reservation);
                sendReservationReceivedEmail(created);
                return ValidationResults.success(created);
            } else {
                logger.debug("Validation errors for reservation create {}: {}", reservation, errors);
                return ValidationResults.invalid(reservation, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating reservation {}", reservation, e);
            return ValidationResults.error();
        }
    }

    @Override
    public Boolean existsBySeatingId(Long seatingId) {
        return reservationRepository.existsBySeatingId(seatingId);
    }

    @Override
    public Boolean existsBySeatingEventId(Long eventId) {
        return reservationRepository.existsBySeatingEventId(eventId);
    }

    @Override
    public ValidatedResult<Reservation> getByUuid(UUID uuid) {
        try {
            return ValidationResults.success(reservationRepository.getByUuid(uuid).orElse(null));
        } catch (Exception e) {
            logger.error("Error retrieving reservation with uuid: {}", uuid, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Reservation>> getAll(String eventName, ReservationStatus status) {
        try {
            return ValidationResults.success(reservationRepository.getAll(eventName, status));
        } catch (Exception e) {
            logger.error("Error retrieving reservations", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Reservation> approve(Long id, Long tableId) {
        try {
            var result = reservationRepository.get(id);
            if (result.isEmpty()) return ValidationResults.error();
            var reservation = result.get();

            reservation.setTableId(tableId);

            var errors = reservationValidationService.validateApprove(reservation);
            if (!errors.isEmpty()) {
                return ValidationResults.invalid(reservation, errors);
            }

            reservation.setStatus(ReservationStatus.APPROVED);
            var updated = reservationRepository.update(reservation);
            sendReservationStatusEmail(updated);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            logger.error("Error approving reservation {}", id, e);
            return ValidationResults.error();
        }
    }

    @Override
    public ValidatedResult<Reservation> deny(Long id) {
        try {
            var result = reservationRepository.get(id);
            if (result.isEmpty()) return ValidationResults.error();
            var reservation = result.get();

            var errors = reservationValidationService.validateDeny(reservation);
            if (!errors.isEmpty()) {
                return ValidationResults.invalid(reservation, errors);
            }

            reservation.setStatus(ReservationStatus.DENIED);
            var updated = reservationRepository.update(reservation);
            sendReservationStatusEmail(updated);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            logger.error("Error denying reservation {}", id, e);
            return ValidationResults.error();
        }
    }

    @Override
    public Result<Collection<Reservation>> getApprovedByEventId(Long eventId) {
        try {
            return ValidationResults.success(reservationRepository.getApprovedByEventId(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving approved reservations for eventId: {}", eventId, e);
            return ValidationResults.error();
        }
    }

    private void sendReservationReceivedEmail(Reservation reservation) {
        if (reservation.getEmail() == null || reservation.getEmail().isBlank()) {
            logger.info("Skipping received email — no email on reservation.");
            return;
        }
        try {
            String eventName = reservation.getSeating() != null && reservation.getSeating().getEvent() != null
                    ? reservation.getSeating().getEvent().getName() : "N/A";
            String seatingDateTime = reservation.getSeating() != null && reservation.getSeating().getStartDateTime() != null
                    ? reservation.getSeating().getStartDateTime().toString() : "N/A";

            String body = "Dear " + reservation.getFirstName() + " " + reservation.getLastName() + ",\n\n"
                    + "We have received your reservation request. Here are the details:\n\n"
                    + "Event Name: " + eventName + "\n"
                    + "Seating Date & Time: " + seatingDateTime + "\n"
                    + "Guest Name: " + reservation.getFirstName() + " " + reservation.getLastName() + "\n"
                    + "Group Size: " + reservation.getGroupSize() + "\n"
                    + "Status: PENDING\n\n"
                    + "We will notify you once your request has been reviewed.\n\n"
                    + "Thank you,\nThe Lima Lounge";

            var emailRequest = new EmailRequest()
                    .setTo(reservation.getEmail())
                    .setSubject("Reservation Request Received - Lima Lounge")
                    .setBody(body);

            if (emailService.sendEmail(emailRequest)) {
                logger.info("Reservation received email sent to: {}", reservation.getEmail());
            } else {
                logger.warn("Failed to send reservation received email to: {}", reservation.getEmail());
            }
        } catch (Exception e) {
            logger.error("Error sending reservation received email", e);
        }
    }

    private void sendReservationStatusEmail(Reservation reservation) {
        if (reservation.getEmail() == null || reservation.getEmail().isBlank()) {
            logger.info("Skipping status email — no email on reservation.");
            return;
        }
        try {
            String eventName = reservation.getSeating() != null && reservation.getSeating().getEvent() != null
                    ? reservation.getSeating().getEvent().getName() : "N/A";
            String seatingDateTime = reservation.getSeating() != null && reservation.getSeating().getStartDateTime() != null
                    ? reservation.getSeating().getStartDateTime().toString() : "N/A";
            String status = reservation.getStatus().toString();

            String body = "Dear " + reservation.getFirstName() + " " + reservation.getLastName() + ",\n\n"
                    + "Your reservation request has been " + status + ".\n\n"
                    + "Event Name: " + eventName + "\n"
                    + "Seating Date & Time: " + seatingDateTime + "\n"
                    + "Guest Name: " + reservation.getFirstName() + " " + reservation.getLastName() + "\n"
                    + "Group Size: " + reservation.getGroupSize() + "\n"
                    + "Status: " + status + "\n\n"
                    + "Thank you,\nThe Lima Lounge";

            var emailRequest = new EmailRequest()
                    .setTo(reservation.getEmail())
                    .setSubject("Reservation " + status + " - Lima Lounge")
                    .setBody(body);

            if (emailService.sendEmail(emailRequest)) {
                logger.info("Reservation status email sent to: {}", reservation.getEmail());
            } else {
                logger.warn("Failed to send reservation status email to: {}", reservation.getEmail());
            }
        } catch (Exception e) {
            logger.error("Error sending reservation status email", e);
        }
    }
}