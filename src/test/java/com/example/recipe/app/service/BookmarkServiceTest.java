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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(BookmarkService.class)
class BookmarkServiceTest {

    @MockBean
    private BookmarkRepository bookmarkRepository;

    @MockBean
    RecipeService recipeService;

    @MockBean
    UserService userService;

    private BookmarkService bookmarkService;

    @BeforeEach
    void setUp() {
        bookmarkService = new BookmarkService(bookmarkRepository, recipeService, userService);
    }

    private final User user = User.builder().name("User 1").id(1L).email("user@gmail.com").build();
    private final Recipe recipe = Recipe.builder().id(1L).name("recipe 1").build();
    private final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private final Bookmark bookmark = Bookmark.builder().user(user).timestamp(now).recipe(recipe).build();
    private final BookmarkRequest bookmarkRequest = BookmarkRequest.builder().recipeId(1L).userId(1L).build();
    private final RecipeResponse recipeResponse = RecipeResponse.builder().id(1L).name("Recipe").build();
    private final BookmarkByDateRequest bookmarkByDateRequest = BookmarkByDateRequest.builder()
            .fromDate(now.minusDays(1).toString()).toDate(now.toString()).build();


    @Test
    void getBookmarksByUserId() {
        doReturn(List.of(bookmark)).when(bookmarkRepository).findAllByUserId(1L);

        List<BookmarkResponse> result = bookmarkService.getBookmarksByUserId(1L);

        assertEquals(bookmark.getId(), result.get(0).getId());
        assertEquals(bookmark.getRecipe().getId(), result.get(0).getRecipeId());
        assertEquals(bookmark.getUser().getId(), result.get(0).getUserId());
        assertEquals(bookmark.getTimestamp().toString(), result.get(0).getTimestamp());
    }

    @Test
    void getBookmarksDetailsByUserId() {
        doReturn(List.of(bookmark)).when(bookmarkRepository).findAllByUserId(1L);

        UserBookmarksResponse result = bookmarkService.getBookmarksDetailsByUserId(1L);

        assertEquals(bookmark.getUser().getId(), result.getUserId());
        assertEquals(bookmark.getRecipe().getId(), result.getRecipes().get(0).getId());
        assertEquals(bookmark.getRecipe().getName(), result.getRecipes().get(0).getName());
    }

    @Test
    void addBookmark() {
        doReturn(user).when(userService).getUser(1L);
        doReturn(recipe).when(recipeService).getRecipeById(1L);
        doReturn(bookmark).when(bookmarkRepository).save(any());

        BookmarkResponse result = bookmarkService.addBookmark(bookmarkRequest);

        assertEquals(bookmark.getId(), result.getId());
        assertEquals(bookmark.getRecipe().getId(), result.getRecipeId());
        assertEquals(bookmark.getUser().getId(), result.getUserId());
        assertEquals(bookmark.getTimestamp().toString(), result.getTimestamp());
    }

    @Test
    void addBookmarkElementPresent() {
        doReturn(user).when(userService).getUser(1L);
        doReturn(recipe).when(recipeService).getRecipeById(1L);
        doReturn(Optional.of(bookmark)).when(bookmarkRepository).findByUserAndRecipe(any(), any());

        assertThrows(ElementPresentException.class, () -> bookmarkService.addBookmark(bookmarkRequest));
    }

    @Test
    void deleteBookmarks() {
        doNothing().when(bookmarkRepository).deleteAllByUserId(1L);

        bookmarkService.deleteBookmarks(1L);

        verify(bookmarkRepository, times(1)).deleteAllByUserId(1L);
    }

    @Test
    void deleteBookmark() {
        doNothing().when(bookmarkRepository).deleteBookmarkByUserIdAndId(1L, 1L);

        bookmarkService.deleteBookmark(1L, 1L);

        verify(bookmarkRepository, times(1)).deleteBookmarkByUserIdAndId(1L, 1L);
    }

    @Test
    void getUserBookmarksByDate() {
        doReturn(List.of(bookmark)).when(bookmarkRepository).findAllByUserIdAndTimestampBetween(any(), any(), any());

        List<BookmarkResponse> result = bookmarkService.getUserBookmarksByDate(1L, bookmarkByDateRequest);

        assertEquals(bookmark.getId(), result.get(0).getId());
        assertEquals(bookmark.getRecipe().getId(), result.get(0).getRecipeId());
        assertEquals(bookmark.getUser().getId(), result.get(0).getUserId());
        assertEquals(bookmark.getTimestamp().toString(), result.get(0).getTimestamp());
    }
}