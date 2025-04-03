package com.rypsk.weeklymenucreator.api.model.entity;

import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
