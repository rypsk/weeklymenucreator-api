package com.rypsk.weeklymenucreator.api.model.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Quantity {
    private double amount;
    private String unit;
}
