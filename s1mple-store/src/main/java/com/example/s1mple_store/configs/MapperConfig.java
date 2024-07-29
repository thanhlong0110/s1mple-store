package com.example.s1mple_store.configs;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.s1mple_store.mappers.IProductMapper;

@Configuration
public class MapperConfig {

    @Bean
    public IProductMapper productMapper() {
        return Mappers.getMapper(IProductMapper.class);
    }
}