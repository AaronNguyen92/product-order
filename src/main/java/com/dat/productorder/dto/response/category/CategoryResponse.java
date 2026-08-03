package com.dat.productorder.dto.response.category;

import com.dat.productorder.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private CategoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
