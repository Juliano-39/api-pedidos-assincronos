package com.juliano.pedidos.category.service;

import com.juliano.pedidos.category.dto.CategoryRequestDto;
import com.juliano.pedidos.category.dto.CategoryResponseDto;
import com.juliano.pedidos.category.model.Category;
import com.juliano.pedidos.category.repository.CategoryRepository;
import com.juliano.pedidos.shared.exception.DuplicateResourceException;
import com.juliano.pedidos.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository){
        this.repository = repository;
    }

    public CategoryResponseDto creation(CategoryRequestDto request){
        // Verifica duplicidade de categoria
        if (repository.findCategoryByName(request.name()).isPresent()){
            throw new DuplicateResourceException("Categoria já possui cadastro");
        }

        // Monta a entidade para ser persistida no banco
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        category = repository.save(category);

        // Monta a responseDto para devolver o body na requisição
        CategoryResponseDto response = new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );

        return response;
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto request) throws DuplicateResourceException {

        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        Optional<Category> duplicate = repository.findCategoryByName(request.name());

        if (duplicate.isPresent() && !duplicate.get().getId().equals(category.getId())){
            throw new DuplicateResourceException("Categoria já possui cadastro");
        }

        category.setName(request.name());
        category.setDescription(request.description());
        category = repository.save(category);

        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription());
    }

    @Transactional
    public void delete(Long id){
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categpria não encontrada"));

        repository.delete(category);
    }

    public CategoryResponseDto findById(Long id){

        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription());
    }


    public Page<CategoryResponseDto> list(Pageable pageable) {

        Page<Category> categories = repository.findAll(pageable);

        return categories.map(category ->
                new CategoryResponseDto(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                ));
    }
}
