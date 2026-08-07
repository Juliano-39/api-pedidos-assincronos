package com.juliano.pedidos.product.service;

import com.juliano.pedidos.category.model.Category;
import com.juliano.pedidos.category.repository.CategoryRepository;
import com.juliano.pedidos.product.dto.ProductRequestDto;
import com.juliano.pedidos.product.dto.ProductResponseDto;
import com.juliano.pedidos.product.model.Product;
import com.juliano.pedidos.product.repository.ProductRepository;
import com.juliano.pedidos.shared.exception.DuplicateResourceException;
import com.juliano.pedidos.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto request){

        if(productRepository.findByName(request.name()).isPresent()){
        throw new DuplicateResourceException("Produto já possui cadastro");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Cartegoria não encontrada"));

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setStatus(request.status());
        product.setCategoryId(category);

        product = productRepository.save(product);

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.isStatus(),
                product.getCategoryId().getId(),
                product.getCategoryId().getName(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto request){

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        Optional<Product> duplicated = productRepository.findByName(request.name());

        if(duplicated.isPresent() && !duplicated.get().getId().equals(product.getId())){
            throw new DuplicateResourceException("Produto já possui cadastro");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setQuantity(request.quantity());
        product.setPrice(request.price());
        product.setStatus(request.status());
        product.setCategoryId(category);

        product = productRepository.save(product);

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.isStatus(),
                product.getCategoryId().getId(),
                product.getCategoryId().getName(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    @Transactional
    public void delete(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        productRepository.delete(product);
    }

    public ProductResponseDto findById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.isStatus(),
                product.getCategoryId().getId(),
                product.getCategoryId().getName(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    public Page<ProductResponseDto> listAll(Pageable pageable){

        Page<Product> products = productRepository.findAll(pageable);

        return products.map(product ->
                new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.isStatus(),
                        product.getCategoryId().getId(),
                        product.getCategoryId().getName(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ));
    }
}
