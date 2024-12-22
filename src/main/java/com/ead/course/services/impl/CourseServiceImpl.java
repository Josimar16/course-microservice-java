package com.ead.course.services.impl;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.exceptions.ConflictException;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    final CourseRepository courseRepository;
    final ModuleRepository moduleRepository;
    final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository courseRepository, ModuleRepository moduleRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public CourseModel save(CourseRecordDto courseRecordDto) {
        var courseAlreadyExists = courseRepository.existsByName(courseRecordDto.name());

        if (courseAlreadyExists) {
            throw new ConflictException("Error: Course name is already taken!");
        }

        var courseModel = new CourseModel();

        BeanUtils.copyProperties(courseRecordDto, courseModel);
        courseModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        return courseRepository.save(courseModel);
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModels = moduleRepository.findAllModulesIntoCourse(courseModel.getId());

        if (!moduleModels.isEmpty()) {
            for (ModuleModel moduleModel : moduleModels) {
                List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(moduleModel.getId());

                if (!lessonModels.isEmpty()) {
                    lessonRepository.deleteAll(lessonModels);
                }
            }

            moduleRepository.deleteAll(moduleModels);
        }

        courseRepository.delete(courseModel);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        Optional<CourseModel> courseModel = courseRepository.findById(courseId);

        if (courseModel.isEmpty()) {
            throw new NotFoundException("Error: Course not found.");
        }

        return courseModel;
    }

    @Override
    public void update(CourseRecordDto courseRecordDto, CourseModel courseModel) {
        BeanUtils.copyProperties(courseRecordDto, courseModel);
        courseModel.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        courseRepository.save(courseModel);
    }
}
