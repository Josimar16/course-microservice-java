package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ModuleRecordDto(
    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Description is required")
    String description
) {
}
