package org.devcourse.resumeme.common.controller;

import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EnumController {

    @GetMapping("/enums")
    public EnumDocsResponse enums() {
        return new EnumDocsResponse(to(null));
    }

    private Map<String, String> to(DocsEnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .collect(Collectors.toMap(DocsEnumType::getType, DocsEnumType::getDescription));
    }

    public record EnumDocsResponse(Map<String, String> enumCode) {

    }

}