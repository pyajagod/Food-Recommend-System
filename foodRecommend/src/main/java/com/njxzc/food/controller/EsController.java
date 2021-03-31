package com.njxzc.food.controller;

import com.njxzc.food.model.Food;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/es")
public class EsController {

//    @Autowired
    private TransportClient client;

    @GetMapping("/show")
    @ResponseBody
    public List<Food> show(@RequestParam(name = "key") String key){
        System.out.println(key);
        QueryBuilder bq = QueryBuilders.matchQuery("name.pinyin", key);
        SearchRequestBuilder searchRequest = client.prepareSearch("").setTypes("folks");

        int start = 0;
        int size = 10;
        searchRequest.setQuery(bq).setFrom(start).setSize(size);

        SearchResponse response = searchRequest.execute().actionGet();
        SearchHits hits = response.getHits();
        ArrayList<Food> esResult = new ArrayList<>();
        for (SearchHit hit: hits) {
            Food food = new Food();
            String name = (String) hit.getSourceAsMap().get("name");
            String genres = (String) hit.getSourceAsMap().get("genres");
            food.setName(name);
            food.setGenres(genres);
            System.out.println("name: " + name + " genres: " + genres);
            esResult.add(food);
        }
        return esResult;
    }
}
