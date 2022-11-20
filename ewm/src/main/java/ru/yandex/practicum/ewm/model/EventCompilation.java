package ru.yandex.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "event_compilations")
@IdClass(EventCompilationPk.class)
public class EventCompilation {
    @Id
    @Column(name = "compilation_id")
    private Long compilationId;
    @Id
    @Column(name = "event_id")
    private Long eventId;
}