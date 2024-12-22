package com.ead.course.controllers;

import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
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
@RequestMapping
public class ModuleController {
    final ModuleService moduleService;
    final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> createModule(
        @PathVariable(value = "courseId") UUID courseId,
        @RequestBody @Valid ModuleRecordDto moduleRecordDto
    ) {
        CourseModel courseModel = courseService.findById(courseId).get();
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleRecordDto, courseModel));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(
        @PathVariable(value = "courseId") UUID courseId,
        SpecificationTemplate.ModuleSpec moduleSpec,
        Pageable pageable
    ) {
        Page<ModuleModel> modules = moduleService.findAllModulesIntoCourse(SpecificationTemplate.moduleCourseId(courseId).and(moduleSpec), pageable);

        return ResponseEntity.ok(modules);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneCourse(
        @PathVariable(value = "courseId") UUID courseId,
        @PathVariable(value = "moduleId") UUID moduleId
    ) {
        return ResponseEntity.ok(moduleService.findModuleIntoCourse(courseId, moduleId).get());
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteCourse(
        @PathVariable(value = "courseId") UUID courseId,
        @PathVariable(value = "moduleId") UUID moduleId
    ) {
        moduleService.findModuleIntoCourse(courseId, moduleId).ifPresent(moduleService::delete);
        return ResponseEntity.ok("Module deleted successfully.");
    }


    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateCourse(
        @PathVariable(value = "courseId") UUID courseId,
        @PathVariable(value = "moduleId") UUID moduleId,
        @RequestBody
        @Valid ModuleRecordDto moduleRecordDto
    ) {
        ModuleModel moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId).get();
        moduleService.update(moduleRecordDto, moduleModel);
        return ResponseEntity.noContent().build();
    }
}
