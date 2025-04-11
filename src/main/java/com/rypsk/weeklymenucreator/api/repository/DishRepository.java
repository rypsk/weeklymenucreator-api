package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Collection<Dish> findByUserId(Long userId);

    List<Dish> findByDietTypeAndDishTypeAndUserId(DietType dietType, DishType dishType, Long userId);
}
