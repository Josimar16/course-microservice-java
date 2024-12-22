package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record LessonRecordDto(
    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Description is required")
    String description,

    @NotBlank(message = "VideoUrl is required")
    String videoUrl
) {
}