package com.eazybytes.jobportal.client.service;

import com.eazybytes.jobportal.dto.PostDto;
import com.eazybytes.jobportal.dto.TodoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange// (url="https://jsonplaceholder.typicode.com/posts")
public interface PostServices {

    @GetExchange
    List<PostDto> findAll();

    @GetExchange("/{id}")
    PostDto findById(@PathVariable Long id);

    @PostExchange
    PostDto create(@RequestBody PostDto post);

    @PutExchange("/{id}")
    PostDto update(@PathVariable Long id, @RequestBody PostDto post);

    @DeleteExchange("/{id}")
    void delete(@PathVariable Long id);
}
