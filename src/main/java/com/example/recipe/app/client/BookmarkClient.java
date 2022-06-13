package com.example.recipe.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bookmark")
public interface BookmarkClient {

    @RequestMapping(method = RequestMethod.GET, value = "/bookmark/{userId}")
    List<Long> getBookmarks(@PathVariable("userId") Long userId,
                            @RequestParam(name = "from", required = false) String fromDate,
                            @RequestParam(name = "to", required = false) String toDate);

}
