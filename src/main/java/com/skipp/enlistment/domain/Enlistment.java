package com.skipp.enlistment.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a student's enlistment in a section.
 *
 * @param studentNumber the studentNumber of the student
 * @param sectionId     the sectionId of the section that the student is enlisted in
 */
public record Enlistment (int studentNumber, String sectionId) {}
