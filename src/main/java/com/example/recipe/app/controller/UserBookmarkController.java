package com.example.recipe.app.controller;

import com.example.recipe.app.model.response.Bookmark;
import com.example.recipe.app.model.response.UserBookmarksResponse;
import com.example.recipe.app.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Bookmark")
public class UserBookmarkController {
    private final BookmarkService bookmarkService;

    UserBookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Element found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Element not found",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Server not ready",
                    content = @Content)})
    @Operation(method = "GetByUserId", description = "Get bookmarks for specific user")
    @GetMapping("/{userId}/bookmark")
    public UserBookmarksResponse getBookmarksByUserId(@PathVariable Long userId,
                                                      @RequestParam(name = "from", required = false) String fromDate,
                                                      @RequestParam(name = "to", required = false) String toDate) {
        return bookmarkService.getUserBookmark(userId, fromDate, toDate);
    }
}
