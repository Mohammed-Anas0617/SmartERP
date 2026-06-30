package com.smarterp.smarterp.dto;

import com.smarterp.smarterp.entity.GroupNature;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDto {

    @NotBlank(message = "Group name is required")
    private String name;

    @NotNull(message = "Group nature is required")
    private GroupNature nature;
}