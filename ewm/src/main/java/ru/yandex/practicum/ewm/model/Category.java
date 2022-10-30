package ru.yandex.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
public class Category extends BaseEntity{
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}
