package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {
    final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> createCourse(@RequestBody @Valid CourseRecordDto courseRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseRecordDto));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(
        SpecificationTemplate.CourseSpec courseSpec,
        Pageable pageable
    ) {
        Page<CourseModel> courses = courseService.findAll(courseSpec, pageable);

//        if (!courses.isEmpty()) {
//            for (CourseModel course : courses.toList()) {
//                course.add(linkTo(methodOn(UsersController.class).getOneUser(user.getId())).withSelfRel());
//            }
//        }

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "id") UUID courseId) {
        return ResponseEntity.ok(courseService.findById(courseId).get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "id") UUID courseId) {
        courseService.findById(courseId).ifPresent(courseService::delete);
        return ResponseEntity.ok("Course deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCourse(
            @PathVariable(value = "id") UUID courseId,
            @RequestBody
            @Valid CourseRecordDto courseRecordDto
    ) {
        CourseModel courseModel = courseService.findById(courseId).get();
        courseService.update(courseRecordDto, courseModel);
        return ResponseEntity.noContent().build();
    }
}
