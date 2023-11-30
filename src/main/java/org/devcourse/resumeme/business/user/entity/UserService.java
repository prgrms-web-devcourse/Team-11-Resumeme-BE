package org.devcourse.resumeme.business.user.entity;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationEventPublisher;
import org.devcourse.resumeme.business.user.service.vo.UserInfoUpdateVo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final MentorApplicationEventPublisher mentorApplicationEventPublisher;

    public User create(User user) {
        User savedUser = userRepository.save(user);
        if (user.getRequiredInfo().getRole().equals(Role.ROLE_PENDING)) {
            mentorApplicationEventPublisher.publishMentorApplicationEvent(savedUser);
        }
        return savedUser;
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        User user = getOne(id);
        user.updateRefreshToken(refreshToken);
    }

    public User getOne(Long userId) {
        return userRepository.findWithPositionsAndFields(userId)
                .orElseThrow(() -> new CustomException(MENTEE_NOT_FOUND));
    }

    public Long update(Long userId, UserInfoUpdateVo updateRequest) {
        User user = getOne(userId);
        user.updateInfos(updateRequest);

        return userId;
    }

    public void deleteRefreshToken(Long id) {
        updateRefreshToken(id, null);
    }

    public List<User> getByIds(List<Long> ids) {
        return userRepository.findAllByIds(ids);
    }

    public void updateRole(Long mentorId, ApplicationProcessType applicationProcessType) {
        mentor.updateRole(type.getRole());
    }

}
