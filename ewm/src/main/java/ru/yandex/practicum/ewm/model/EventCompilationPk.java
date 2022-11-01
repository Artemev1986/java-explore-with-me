package ru.yandex.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EventCompilationPk implements Serializable {
    private Long compilationId;
    private Long eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventCompilationPk that = (EventCompilationPk) o;
        return eventId != null && Objects.equals(eventId, that.eventId)
                && compilationId != null && Objects.equals(compilationId, that.compilationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, compilationId);
    }
}
