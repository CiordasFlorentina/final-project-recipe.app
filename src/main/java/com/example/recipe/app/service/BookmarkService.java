package com.example.recipe.app.service;

import com.example.recipe.app.exeption.ElementPresentException;
import com.example.recipe.app.model.entity.Bookmark;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.BookmarkByDateRequest;
import com.example.recipe.app.model.request.BookmarkRequest;
import com.example.recipe.app.model.response.BookmarkResponse;
import com.example.recipe.app.model.response.RecipeResponse;
import com.example.recipe.app.model.response.UserBookmarksResponse;
import com.example.recipe.app.repository.BookmarkRepository;
import com.example.recipe.app.utils.Converter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final RecipeService recipeService;
    private final UserService userService;

    BookmarkService(BookmarkRepository bookmarkRepository, RecipeService recipeService, UserService userService) {
        this.bookmarkRepository = bookmarkRepository;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    public List<BookmarkResponse> getBookmarksByUserId(Long userId) {
        return bookmarkRepository.findAllByUserId(userId)
                .stream()
                .map(Converter::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserBookmarksResponse getBookmarksDetailsByUserId(Long userId) {
        return UserBookmarksResponse.builder().userId(userId).recipes(
                bookmarkRepository.findAllByUserId(userId)
                        .stream()
                        .map(bookmark -> RecipeResponse.builder()
                                .id(bookmark.getRecipe().getId())
                                .name(bookmark.getRecipe().getName())
                                .build()
                        ).collect(Collectors.toList())
        ).build();
    }

    public BookmarkResponse addBookmark(BookmarkRequest bookmarkReq) {
        User user = userService.getUser(bookmarkReq.getUserId());
        Recipe recipe = recipeService.getRecipeById(bookmarkReq.getRecipeId());

        if (bookmarkRepository.findByUserAndRecipe(user, recipe).isPresent()) {
            throw new ElementPresentException("Bookmark");
        }
        return Converter.mapToResponse(
                bookmarkRepository.save(
                        Bookmark.builder().user(user).timestamp(LocalDateTime.now()).recipe(recipe).build()
                )
        );
    }

    @Transactional
    public void deleteBookmarks(Long userId) {
        bookmarkRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        bookmarkRepository.deleteBookmarkByUserIdAndId(userId, bookmarkId);
    }

    public List<BookmarkResponse> getUserBookmarksByDate(Long userId, BookmarkByDateRequest bookmarkByDateRequest) {
        LocalDateTime fromDate = LocalDateTime.parse(bookmarkByDateRequest.getFromDate());
        LocalDateTime toDate = LocalDateTime.parse(bookmarkByDateRequest.getToDate());
        return bookmarkRepository.findAllByUserIdAndTimestampBetween(
                userId, fromDate, toDate
        ).stream().map(Converter::mapToResponse).collect(Collectors.toList());
    }
}
