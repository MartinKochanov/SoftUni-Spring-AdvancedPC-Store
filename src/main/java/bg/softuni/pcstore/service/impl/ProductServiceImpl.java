package bg.softuni.pcstore.service.impl;

import bg.softuni.pcstore.model.dto.NewProductDTO;
import bg.softuni.pcstore.model.entity.ProductEntity;
import bg.softuni.pcstore.model.entity.SpecificationEntity;
import bg.softuni.pcstore.model.enums.ProductTypeEnum;
import bg.softuni.pcstore.repository.ProductRepository;
import bg.softuni.pcstore.repository.SpecificationRepository;
import bg.softuni.pcstore.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final SpecificationRepository specificationRepository;

    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, SpecificationRepository specificationRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.specificationRepository = specificationRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void addNewProduct(@Valid NewProductDTO newProductDTO, String productName) {


        ProductEntity product = map(newProductDTO, productName);

        try {
            product.setImage(newProductDTO.getImage().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SpecificationEntity specification = modelMapper.map(newProductDTO, SpecificationEntity.class);
        specificationRepository.save(specification);
        product.setSpecifications(specification);
        productRepository.save(product);
    }

    private ProductEntity map(NewProductDTO newProductDTO, String productName) {
        return new ProductEntity()
                .setTypeProduct(ProductTypeEnum.valueOf(productName))
                .setManufacturer(newProductDTO.getManufacturer())
                .setModel(newProductDTO.getModel())
                .setDescription(newProductDTO.getDescription())
                .setPrice(newProductDTO.getPrice());
    }
}
