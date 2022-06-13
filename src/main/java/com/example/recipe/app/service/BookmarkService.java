package com.example.recipe.app.service;

import com.example.recipe.app.client.BookmarkClient;
import com.example.recipe.app.exeption.BadRequest;
import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.exeption.ServerException;
import com.example.recipe.app.model.response.RecipeResponse;
import com.example.recipe.app.model.response.UserBookmarksResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {
    private final BookmarkClient bookmarkClient;
    private final RecipeService recipeService;

    public BookmarkService(BookmarkClient bookmarkClient, RecipeService recipeService) {
        this.bookmarkClient = bookmarkClient;
        this.recipeService = recipeService;
    }

    public UserBookmarksResponse getUserBookmark(Long userId, String fromDate, String toDate) {
        List<Long> bookmarks = retrieveBookmarks(userId, fromDate, toDate);
        List<RecipeResponse> recipes = recipeService.getRecipesByIds(bookmarks);
        return UserBookmarksResponse.builder().userId(userId).recipes(recipes).build();
    }

    Logger log = LoggerFactory.getLogger(BookmarkService.class);

    public List<Long> retrieveBookmarks(Long userId, String fromDate, String toDate) {
        log.info("Request to bookmark for userId {}", userId);
        try {
            return bookmarkClient.getBookmarks(userId, fromDate, toDate);
        } catch (FeignException.FeignClientException.NotFound exception) {
            throw new NotFoundException("Bookmarks not found for userId " + userId);
        } catch (FeignException.FeignServerException e) {
            throw new ServerException("Cannot reach bookmark service");
        } catch (FeignException.FeignClientException exception) {
            throw new BadRequest("Bad request");
        }
    }
}
