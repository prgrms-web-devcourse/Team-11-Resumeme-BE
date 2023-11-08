package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.ForeignLanguageService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageRequestDto;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ForeignLanguageController {

    private final ForeignLanguageService foreignLanguageService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/foreign-languages")
    public IdResponse createForeignLanguage(@PathVariable Long resumeId, @RequestBody ForeignLanguageRequestDto request) {
        Resume resume = resumeService.getOne(resumeId);
        ForeignLanguage foreignLanguage = request.toEntity(resume);

        return new IdResponse(foreignLanguageService.create(foreignLanguage));
    }

    @GetMapping("/{resumeId}/foreign-languages")
    public List<ForeignLanguageResponse> getForeignLanguages(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        List<ForeignLanguage> languages = resume.getForeignLanguage();

        return languages.stream()
                .map(ForeignLanguageResponse::new)
                .toList();
    }

}