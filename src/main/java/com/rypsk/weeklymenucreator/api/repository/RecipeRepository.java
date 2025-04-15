package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Collection<Recipe> findByUserId(Long userId);

    List<Recipe> findAllByIsPublic(boolean isPublic);

    @Query("SELECT r FROM Recipe r where r.isPublic = true OR r.user.id = :userId")
    List<Recipe> findAvailableForUser(@Param("userId") Long userId);
}
