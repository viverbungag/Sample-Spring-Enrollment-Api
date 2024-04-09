package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.dto.SectionDto;
import com.skipp.enlistment.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

// TODO What stereotype annotation should be put here?
@RequestMapping("/sections")
@RestController
public class SectionController {

    private final SectionService sectionServiceImpl;

    // TODO What bean/s should be wired here?
    @Autowired
    public SectionController(SectionService sectionServiceImpl) {
        this.sectionServiceImpl = sectionServiceImpl;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    public Collection<SectionDto> getAllSections() {
        // TODO implement this handler
        List<SectionDto> sections = sectionServiceImpl.findAllSections().stream()
                .map((Section section) -> new SectionDto(section, false))
                .toList();
        return sections;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    @GetMapping("/{sectionId}")
    public SectionDto getSection(@PathVariable String sectionId) {
        // TODO implement this handler
        Section section = sectionServiceImpl.findById(sectionId, true);
        SectionDto sectionDto = new SectionDto(section, true);
        return sectionDto;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    public SectionDto createSection(@RequestBody SectionDto section) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public SectionDto updateSection(@RequestBody SectionDto section) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public void deleteSection(@PathVariable String sectionId) {
        // TODO implement this handler
    }

}
