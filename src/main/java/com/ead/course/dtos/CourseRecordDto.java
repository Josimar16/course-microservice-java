package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseRecordDto(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "Status is required")
    CourseStatus courseStatus,

    @NotNull(message = "Level is required")
    CourseLevel courseLevel,

    @NotNull(message = "Instructor is required")
    UUID userInstructor,

    String imageUrl
) {
}
