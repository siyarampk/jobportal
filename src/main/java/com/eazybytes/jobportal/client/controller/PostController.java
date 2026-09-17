package com.eazybytes.jobportal.client.controller;

import com.eazybytes.jobportal.client.service.PostServices;
import com.eazybytes.jobportal.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostServices postServices;

    @GetMapping
    ResponseEntity<List<PostDto>> findAll() {
        return ResponseEntity.ok(postServices.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<PostDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postServices.findById(id));
    }

    @PostMapping
    ResponseEntity<PostDto> create(@RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postServices.create(postDto));
    }

    @PutMapping("/{id}")
    ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.OK).body(postServices.update(id, postDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        postServices.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }

}
