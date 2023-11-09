package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.repository.ComponentRepository;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;

    public Long create(Component block, BlockType type) {
        Long resumeId = block.getResumeId();
        Optional<Component> existBlock1 = componentRepository.findExistBlock(resumeId, type.name());

        if (existBlock1.isPresent()) {
            Component component = existBlock1.get();
            component.addSubComponent(block);

            return component.getId();
        }

        return componentRepository.save(new Component(type.name(), null, null, null, resumeId, List.of(block))).getId();
    }

    public List<Component> getAll(Long resumeId) {
        return componentRepository.findAllByResumeId(resumeId).stream()
                .filter(component -> component.getComponent() == null)
                .toList();
    }

}