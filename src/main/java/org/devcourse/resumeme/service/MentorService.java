package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.ApplicationProcessType;
import org.devcourse.resumeme.controller.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MentorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.MENTOR_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final MentorApplicationEventPublisher mentorApplicationEventPublisher;

    @Transactional(readOnly = true)
    public Mentor getOne(Long mentorId) {
        return mentorRepository.findWithPositions(mentorId)
                .orElseThrow(() -> new CustomException(MENTOR_NOT_FOUND));
    }

    public Mentor create(Mentor mentor) {
        Mentor savedMentor = mentorRepository.save(mentor);
        mentorApplicationEventPublisher.publishMentorApplicationEvent(savedMentor);
        return savedMentor;
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        Mentor findMentor = getOne(id);
        findMentor.updateRefreshToken(refreshToken);
    }

    public void updateRole(Long mentorId, ApplicationProcessType type) {
        Mentor mentor = getOne(mentorId);
        mentor.updateRole(type.getRole());
    }

    public Long update(Long mentorId, MentorInfoUpdateRequest mentorInfoUpdateRequest) {
        Mentor mentor = getOne(mentorId);
        mentor.updateInfos(mentorInfoUpdateRequest);

        return mentor.getId();
    }

}
