package com.example.recipe.app.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class BookmarkByDateRequest {
    @NotNull
    private String fromDate;

    @NotNull
    private String toDate;

}
