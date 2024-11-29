package com.example.demo_park_api.web.dto.mapper;

import com.example.demo_park_api.web.dto.PageableDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public  static PageableDto pageableDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}
