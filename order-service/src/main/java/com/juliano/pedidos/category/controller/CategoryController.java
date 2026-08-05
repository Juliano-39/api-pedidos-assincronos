package com.juliano.pedidos.category.controller;

import com.juliano.pedidos.category.dto.CategoryRequestDto;
import com.juliano.pedidos.category.dto.CategoryResponseDto;
import com.juliano.pedidos.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service){
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto request){

        CategoryResponseDto response = service.creation(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long id,
                                                      @Valid @RequestBody CategoryRequestDto request){

        CategoryResponseDto response = service.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id){

        CategoryResponseDto response = service.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Page<CategoryResponseDto>> list(
            @PageableDefault(
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC)Pageable pageable){

        return ResponseEntity.ok(service.list(pageable));
    }
}



