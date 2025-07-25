package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));

        //convert Product DTO to Product
        Product product = modelMapper.map(productDTO, Product.class);

        // set the data
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        //save the data in the DB.
        Product savedProduct = productRepository.save(product);

        //Convert Product to Product DTO
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return savedProductDTO;
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products =  productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOs = products.stream().map(product->modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productDTOS = products.stream().map(product->modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        // find product from database
        Product productFromDb = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));

        // convert the Product DTO to Product
        Product product = modelMapper.map(productDTO, Product.class);

        // Update the product info with one in the request body
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        productFromDb.setSpecialPrice(specialPrice);

        //save to database
        Product savedProduct = productRepository.save(productFromDb);

        //convert to DTO
        ProductDTO updateProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return updateProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        ProductDTO deletedProductDTO = modelMapper.map(product, ProductDTO.class);
        return deletedProductDTO;
    }
}
