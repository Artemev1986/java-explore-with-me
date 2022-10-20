package ru.practicum.ewm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.model.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    private final User user = new User();
    private final Category category1 = new Category();
    private final Category category2 = new Category();
    private final Category category3 = new Category();
    private final Location location = new Location();
    private final Event event = new Event();

    private final Pageable page = PageRequest.of(0, 10);

    @BeforeEach
    void beforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");
        entityManager.persist(user);

        category1.setName("Name of Category");
        entityManager.persist(category1);

        category2.setName("Name of Category2");
        entityManager.persist(category2);

        category3.setName("Name of Category3");
        entityManager.persist(category3);

        location.setLat(20F);
        location.setLon(30F);

        event.setTitle("Event1");
        event.setAnnotation("Event annotation");
        event.setDescription("Event description");
        event.setCategory(category2);
        event.setConfirmedRequests(10L);
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(LocalDateTime.now().plusHours(3));
        event.setInitiator(user);
        event.setLocation(location);
        event.setPaid(true);
        event.setParticipantLimit(0);
        event.setPublishedOn(null);
        event.setRequestModeration(false);
        event.setState(EventState.PUBLISHED);
        event.setViews(0L);

        entityManager.persist(event);

    }

    @Test
    void searchEventByText() {
        List<Event> events = eventRepository.searchEventByText(
                "anno",
                List.of(1L,2L),
                true,
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(5),
                true,
                page
                );

        assertThat(events).isEqualTo(List.of(event));
    }

    @Test
    void getEventByText() {
        Event eventFromMemory = eventRepository.findEventByIdAndState(1L, EventState.PUBLISHED);
        assertThat(event).isEqualTo(eventFromMemory);
    }
}