
```mermaid
%% Sprint 1

classDiagram
    namespace Domain {

        class Result~T~ {
            <<interface>>
            +getValue() T
            +isEmpty() boolean
            +hasValue() boolean
            +isSuccessful() boolean
            +isError() boolean
        }

        class HasValidationErrors {
            <<interface>>
            +getValidationErrors() Collection~ValidationError~
            +isInvalid() boolean
        }

        class ValidatedResult~T~ {
            <<interface>>
            +getValidationErrors() Collection~ValidationError~
            +isInvalid() boolean
            +isSuccessful() boolean
        }

        class ValidationError {
            +String field
            +String message
            +Object value
        }


        class Event {
            +Long id
            +String name
            +String description
            +LocalDate startDate
            +LocalDate endDate
            +Integer durationMinutes
            +BigDecimal price
            +Boolean active
            +Boolean archived
            +LocalDateTime createdDate
            +LocalDateTime updatedDate
            +Long version
        }
        class DiningTable {
            +Long id
            +String name
            +Integer capacity
            +LocalDateTime createdDate
            +Long version
        }

        class Seating {
            +Long id
            +String name
            +LocalDateTime startDateTime
            +Integer durationMinutes
            +LocalDateTime createdDate
            +List~DiningTable~ tables
            +getEventId() Long
            +Event event
            +Long version
        }
    }
    
    namespace Application {
    
        class EventService{
            <<interface>>
            +getAll() Result~List~Event~~
            +get(Long id) Result~Event~
            +create(Event event) Result~Event~
            +update(Event event) Result~Event~
            +delete(Long id) Result~Void~
            +archive(Long id) Result~Void~
            +search(String name, LocalDate startDate, LocalDate endDate) Result~List~Event~~
            +exists(String name) Result~Boolean~            
        }

        class EventValidationService {
            +validate(Event event) Collection~ValidationError~
            +validateDates(Event event) Collection~ValidationError~
            +validateUniqueName(Event event) Collection~ValidationError~
        }

        class EventRepository {
            <<interface>>
            +getAll() List~Event~
            +get(Long id) Optional~Event~
            +create(Event event) Event
            +update(Event event) Event
            +archive(Long id) void
            +delete(Long id) void
            +search(String name, LocalDate startDate, LocalDate endDate) List~Event~
            +exists(String name) boolean
        }


        class DiningTableService {
            <<interface>>
            +getAll() Result~List~DiningTable~~
            +get(Long id) Result~DiningTable~
            +create(DiningTable table) Result~DiningTable~
            +update(DiningTable table) Result~DiningTable~
            +delete(Long id) Result~Void~
        }

        class DiningTableValidationService {
            +validate(DiningTable table) Collection~ValidationError~
        }

        class DiningTableRepository {
            <<interface>>
            +getAll() List~DiningTable~
            +get(Long id) Optional~DiningTable~
            +create(DiningTable table) DiningTable
            +update(DiningTable table) DiningTable
            +delete(Long id) void
        }
        
        class SeatingService {
            <<interface>>
            +getAll() Result~List~Seating~~
            +getAll(Long eventId) Result~List~Seating~~
            +get(Long id) Result~Seating~
            +create(Seating seating) Result~Seating~
        }
        
        class SeatingValidationService {
            +validate(Seating seating) Collection~ValidationError~
            +validateTableAvailability(Seating seating) Collection~ValidationError~
        }

        class SeatingRepository {
            <<interface>>
            +getAll() List~Seating~
            +getAll(Long eventId) List~Seating~
            +get(Long id) Optional~Seating~
            +create(Seating seating) Seating
            +isTableBooked(Long tableId, LocalDateTime start, LocalDateTime end) boolean
        }
    }

    namespace Persistence {
        class EventEntity {
            +Long id
            +String name
            +String description
            +LocalDate startDate
            +LocalDate endDate
            +Integer durationMinutes
            +BigDecimal price
            +Boolean active
            +Boolean archived
            +LocalDateTime createdAt
            +LocalDateTime updatedAt
            +Long version
        }

        class DiningTableEntity {
            +Long id
            +String name
            +Integer capacity
            +LocalDateTime createdAt
            +Long version
        }

        class SeatingEntity {
            +Long id
            +String name
            +LocalDateTime startDateTime
            +Integer durationMinutes
            +LocalDateTime createdAt
            +EventEntity event
            +List~DiningTableEntity~ tables
            +Long version
        }
    }

    Event "1" --> "0..*" Seating : has
    Seating "1" --> "1" Event : belongsTo
    Seating "1" --> "1..*" DiningTable : uses
%%    Over time it will be a many to many relationship, since each dining table would have many seating
    DiningTable "0..*" --> "1..*" Seating : assigned to

    EventEntity "1" --> "0..*" SeatingEntity : has
    SeatingEntity "1" --> "1" EventEntity : belongsTo
    SeatingEntity "1" --> "1..*" DiningTableEntity : uses
    DiningTableEntity "0..*" --> "1..*" SeatingEntity : assignedTo

    EventService ..> Result : uses
    DiningTableService ..> Result : uses
    SeatingService ..> Result : uses
    
    EventService ..> EventValidationService : is validated by
    DiningTableService ..> DiningTableValidationService : is validated by
    SeatingService ..> SeatingValidationService : is validated by

    %% Inheritance Relationships
    Result~T~ <|-- ValidatedResult~T~ : extends
    HasValidationErrors <|-- ValidatedResult~T~ : extends

    %% Dependencies (Returns/Uses)
    HasValidationErrors ..> ValidationError : uses
    ValidatedResult~T~ ..> ValidationError : uses

    %% Repository uses Domain
    EventRepository ..> Event : uses
    DiningTableRepository ..> DiningTable : uses
    SeatingRepository ..> Seating : uses
```
