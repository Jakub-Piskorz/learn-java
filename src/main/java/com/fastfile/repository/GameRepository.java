package com.fastfile.repository;

import com.fastfile.model.Game;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
    @Nullable
    Game findBySlug(String slug);
}
