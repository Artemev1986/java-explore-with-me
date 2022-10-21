package ru.yandex.practicum.ewm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.ewm.model.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class CompilationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompilationRepository compilationRepository;

    private final User user = new User();
    private final Category category = new Category();
    private final Location location = new Location();
    private final Event event = new Event();
    private final Compilation compilation = new Compilation();

    @BeforeEach
    void beforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");
        entityManager.persist(user);

        category.setName("Name of Category");
        entityManager.persist(category);

        location.setLat(20F);
        location.setLon(30F);

        event.setTitle("Event1");
        event.setAnnotation("Event annotation");
        event.setDescription("Event description");
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(LocalDateTime.now().plusHours(3));
        event.setInitiator(user);
        event.setLocation(location);
        event.setPaid(false);
        event.setParticipantLimit(100);
        event.setPublishedOn(null);
        event.setRequestModeration(false);
        event.setState(EventState.PENDING);
        event.setViews(0L);

        entityManager.persist(event);

        compilation.setTitle("Compilation1");
        compilation.setPinned(false);
        compilation.setEvents(Set.of(event));

        entityManager.persist(compilation);
    }

    @Test
    void addCompilationTest() {
        Compilation compilationLoad = compilationRepository.getReferenceById(1L);
        assertThat(compilation).isEqualTo(compilationLoad);
    }
}