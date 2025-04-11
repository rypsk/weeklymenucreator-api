package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.WeeklyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface WeeklyMenuRepository extends JpaRepository<WeeklyMenu, Long> {
    Collection<WeeklyMenu> findByUserId(Long userId);
}
