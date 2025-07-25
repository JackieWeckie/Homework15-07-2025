package com.morozovnikita16072025.Controller;

import com.morozovnikita16072025.Model.MovieApiResponse;
import com.morozovnikita16072025.Model.Search;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class Controller {
    private final String apiKey = "266b43cd";
    private final String baseUrl = "https://www.omdbapi.com";

    @GetMapping("/{title}")
    public ResponseEntity<?> getAll(@PathVariable(name = "title") String title) {
        String url = baseUrl + "/?s=" + title + "&apiKey=" + apiKey + "&page=";
        RestTemplate restTemplate = new RestTemplate();
        List<Search> arr = new ArrayList<>();
        System.out.println("Send request # " + 1);
        ResponseEntity<MovieApiResponse> response = restTemplate.getForEntity(url + 1, MovieApiResponse.class);
        MovieApiResponse result;

        if (response.getStatusCode().is2xxSuccessful()) {
            result = response.getBody();
            if (result != null) {
                arr.addAll(result.Search);
            }

            int totalResult = 0;
            if (result != null) {
                totalResult = Integer.parseInt(result.totalResults);
            }
            int pages = (int) Math.ceil(totalResult / 10.0);
            System.out.println("Total result => " + totalResult);
            System.out.println("Total pages  => " + pages);

            for (int i = 2; i <= pages; i++) {
                System.out.println("Send request # " + i);
                response = restTemplate.getForEntity(url + i, MovieApiResponse.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    result = response.getBody();
                    if (result != null) {
                        arr.addAll(result.Search);
                    }
                }
            }

            if (result != null) {
                result.Search = new ArrayList<>(arr);
            }

            if (result != null) {
                result.totalPages = pages;
            }

            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
