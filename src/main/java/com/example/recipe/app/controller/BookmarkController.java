package com.example.recipe.app.controller;

import com.example.recipe.app.model.request.BookmarkByDateRequest;
import com.example.recipe.app.model.request.BookmarkRequest;
import com.example.recipe.app.model.response.BookmarkResponse;
import com.example.recipe.app.model.response.UserBookmarksResponse;
import com.example.recipe.app.service.BookmarkService;
//import io.swagger.annotations.Api; # DEPRECATED
//import io.swagger.annotations.ApiOperation; # DEPRECATED
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookmark")
// https://springdoc.org/migrating-from-springfox.html
@Tag(name = "Bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "GetByUserId", description = "Get bookmarks for specific user")
    public List<BookmarkResponse> getBookmarksByUserId(@PathVariable Long userId) {
        return bookmarkService.getBookmarksByUserId(userId);
    }

    @GetMapping("/{userId}/details")
    @Operation(summary = "GetDetailsByUserId", description = "Get bookmarks with recipe details for specific user")
    public UserBookmarksResponse getBookmarksDetailsByUserId(@PathVariable Long userId) {
        return bookmarkService.getBookmarksDetailsByUserId(userId);
    }

    @PostMapping
    @Operation(summary = "Add", description = "Add new bookmark")
    public BookmarkResponse addBookmark(@Valid @RequestBody BookmarkRequest bookmark) {
        return bookmarkService.addBookmark(bookmark);
    }

    @PostMapping("/{userId}")
    @Operation(summary = "GetUserBookmarksByDate", description = "Get bookmarks between dates for specific user")
    public List<BookmarkResponse> getUserBookmarksByDate(
            @PathVariable Long userId,
            @Valid @RequestBody BookmarkByDateRequest bookmarkByDateRequest) {
        return bookmarkService.getUserBookmarksByDate(userId, bookmarkByDateRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "DeleteAllByUserId", description = "Remove all bookmarks for user")
    public ResponseEntity<Long> deleteBookmarks(@PathVariable Long userId) {
        bookmarkService.deleteBookmarks(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{userId}/{bookmarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "DeleteByUserId", description = "Remove bookmark for user")
    public ResponseEntity<Long> deleteBookmark(@PathVariable Long userId, @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(userId, bookmarkId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
