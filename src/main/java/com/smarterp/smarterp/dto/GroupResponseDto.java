package com.smarterp.smarterp.dto;

import com.smarterp.smarterp.entity.GroupNature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponseDto {

    private Long id;
    private String name;
    private GroupNature nature;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}