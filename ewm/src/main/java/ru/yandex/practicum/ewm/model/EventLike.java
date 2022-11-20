package ru.yandex.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "event_likes")
@IdClass(EventLikePk.class)
public class EventLike {
    @Id
    private Long eventId;
    @Id
    private Long userId;
    @Column(name = "is_positive", nullable = false)
    private Boolean positive;
}