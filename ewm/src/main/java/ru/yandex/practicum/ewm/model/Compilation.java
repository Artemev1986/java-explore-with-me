package ru.yandex.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "compilations")
public class Compilation extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "is_pinned", nullable = false)
    private Boolean pinned;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "event_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    @ToString.Exclude
    private Set<Event> events = new HashSet<>();
}
