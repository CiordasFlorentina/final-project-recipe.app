package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Bookmark;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.BookmarkByDateRequest;
import com.example.recipe.app.model.request.BookmarkRequest;
import com.example.recipe.app.model.response.BookmarkResponse;
import com.example.recipe.app.model.response.RecipeResponse;
import com.example.recipe.app.model.response.UserBookmarksResponse;
import com.example.recipe.app.service.BookmarkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

    @MockBean
    private BookmarkService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final User user = User.builder().name("User 1").id(1L).email("user@gmail.com").build();
    private final Recipe recipe = Recipe.builder().id(1L).name("recipe 1").build();
    private final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private final Bookmark bookmark = Bookmark.builder().user(user).timestamp(now).recipe(recipe).build();
    private final BookmarkResponse bookmarkResponse = BookmarkResponse.builder().id(1L).timestamp(now.toString())
            .userId(1L).recipeId(1L).build();
    private final BookmarkRequest bookmarkRequest = BookmarkRequest.builder().recipeId(1L).userId(1L).build();
    private final RecipeResponse recipeResponse = RecipeResponse.builder().id(1L).name("Recipe").build();
    private final UserBookmarksResponse userBookmarksResponse = UserBookmarksResponse.builder().userId(1L)
            .recipes(List.of(recipeResponse)).build();
    private final BookmarkByDateRequest bookmarkByDateRequest = BookmarkByDateRequest.builder()
            .fromDate(now.minusDays(1).toString()).toDate(now.toString()).build();


    @Test
    @DisplayName("GET /bookmark/{userId}")
    void getBookmarksByUserId() throws Exception {
        doReturn(List.of(bookmarkResponse)).when(service).getBookmarksByUserId(1L);

        mockMvc.perform(get("/bookmark/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userId").value("1"))
                .andExpect(jsonPath("$[0].recipeId").value("1"))
                .andExpect(jsonPath("$[0].timestamp").value(now.toString()));
    }

    @Test
    @DisplayName("GET /bookmark/{userId}/details")
    void getBookmarksDetailsByUserId() throws Exception {
        doReturn(userBookmarksResponse).when(service).getBookmarksDetailsByUserId(1L);

        mockMvc.perform(get("/bookmark/{userId}/details", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.recipes.[0].id").value("1"))
                .andExpect(jsonPath("$.recipes.[0].name").value("Recipe"));
    }

    @Test
    @DisplayName("POST /bookmark")
    void addBookmark() throws Exception {
        doReturn(bookmarkResponse).when(service).addBookmark(bookmarkRequest);

        mockMvc.perform(post("/bookmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookmarkResponse)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.recipeId").value("1"))
                .andExpect(jsonPath("$.timestamp").value(now.toString()));
    }

    @Test
    @DisplayName("POST /bookmark/{userId}")
    void getUserBookmarksByDate() throws Exception {
        doReturn(List.of(bookmarkResponse)).when(service).getUserBookmarksByDate(1L, bookmarkByDateRequest);

        mockMvc.perform(post("/bookmark/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookmarkByDateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userId").value("1"))
                .andExpect(jsonPath("$[0].recipeId").value("1"))
                .andExpect(jsonPath("$[0].timestamp").value(now.toString()));

    }

    @Test
    @DisplayName("DELETE /bookmark/{userId}")
    void deleteBookmarks() throws Exception {
        doNothing().when(service).deleteBookmarks(1L);

        mockMvc.perform(delete("/bookmark/{userId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /bookmark/{userId}/{bookmarkId}")
    void deleteBookmark() throws Exception {
        doNothing().when(service).deleteBookmark(1L, 1L);

        mockMvc.perform(delete("/bookmark/{userId}/{bookmarkId}", 1, 1))
                .andExpect(status().isNoContent());

    }
}