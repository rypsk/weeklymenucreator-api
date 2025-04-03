package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {
    Collection<DailyMenu> findByUserId(Long userId);
}
