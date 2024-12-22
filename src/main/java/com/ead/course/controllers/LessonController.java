package com.ead.course.controllers;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LessonController {
    final ModuleService moduleService;
    final LessonService lessonService;

    public LessonController(ModuleService moduleService, LessonService lessonService) {
        this.moduleService = moduleService;
        this.lessonService = lessonService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(
        @PathVariable(value = "moduleId") UUID moduleId,
        @RequestBody @Valid LessonRecordDto lessonRecordDto
    ){
        var moduleModel = moduleService.findById(moduleId).get();

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonRecordDto, moduleModel));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(
        @PathVariable(value = "moduleId") UUID moduleId,
        SpecificationTemplate.LessonSpec lessonSpec,
        Pageable pageable
    ){
        Page<LessonModel> lessons = lessonService.findAllLessonsIntoModule(SpecificationTemplate.lessonModuleId(moduleId).and(lessonSpec), pageable);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(
        @PathVariable(value = "moduleId") UUID moduleId,
        @PathVariable(value = "lessonId") UUID lessonId
    ){
        LessonModel lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId).get();
        return ResponseEntity.ok(lessonModel);
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(
        @PathVariable(value = "moduleId") UUID moduleId,
        @PathVariable(value = "lessonId") UUID lessonId
    ){
        lessonService.findLessonIntoModule(moduleId, lessonId).ifPresent(lessonService::delete);
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(
        @PathVariable(value = "moduleId") UUID moduleId,
        @PathVariable(value = "lessonId") UUID lessonId,
        @RequestBody @Valid LessonRecordDto lessonRecordDto
    ){
        var lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId).get();
        lessonService.update(lessonRecordDto, lessonModel);
        return ResponseEntity.noContent().build();
    }
}
