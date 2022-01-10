package com.example.recipe.app.repository;

import com.example.recipe.app.model.entity.Bookmark;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByUserId(Long userId);

    Optional<Bookmark> findByUserAndRecipe(User user, Recipe recipe);

    List<Bookmark> findAllByUserIdAndTimestampBetween(Long userId, LocalDateTime fromDate, LocalDateTime toDate);

    void deleteBookmarkByUserIdAndId(Long userId, Long bookmarkId);

    void deleteAllByUserId(Long userId);
}
