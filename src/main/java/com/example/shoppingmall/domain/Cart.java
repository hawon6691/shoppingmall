package com.example.shoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("carts")
public class Cart {

    @Id
    private Long id;

    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
