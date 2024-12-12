package de.thu.thutorium.api.transferObjects;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for the CourseCategory entity.
 * <p>
 * This class holds the details of a course category, including the category name.
 * Categories help organize courses into specific groups, making it easier for students to find and select courses
 * based on their interests or field of study.
 * </p>
 */
@Data
public class CourseCategoryDTO {
    /**
     * The name of the course category.
     * <p>
     * This field represents the name of the category to which the course belongs (e.g., "Math", "Science", etc.).
     * Categories help classify courses, making it easier for students to browse and search for courses in specific fields.
     * </p>
     */
    private String categoryName;
}