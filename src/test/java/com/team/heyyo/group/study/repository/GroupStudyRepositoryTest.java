package com.team.heyyo.group.study.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.domain.GroupStudyLike;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import com.team.heyyo.group.study.repository.groupstudylike.GroupStudyLikeRepository;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupStudyRepositoryTest {

    @Autowired
    GroupStudyRepository groupStudyRepository;

    @Autowired
    GroupStudyLikeRepository groupStudyLikeRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("그룹스터디 좋아요 가장많은 순으로 반환한다.")
    @Test
    void group_study_like_desc() {
        //given
        final long groupStudy1Id = 1L;
        final long groupStudy2Id = 2L;
        final long groupStudy3Id = 3L;

        createThreeGroupStudy();

        threeLikesToGroupStudy1(groupStudy1Id);
        twoLikesToGroupStudy2(groupStudy2Id);
        oneLikesToGroupStudy3(groupStudy3Id);

        //when
        List<GroupStudy> groupStudiesOrderedByMostLikes = groupStudyRepository.findGroupStudiesOrderedByMostLikesFromToday();

        //then
        assertThat(groupStudiesOrderedByMostLikes.get(0).getGroupStudyId()).isEqualTo(groupStudy1Id);
        assertThat(groupStudiesOrderedByMostLikes.get(1).getGroupStudyId()).isEqualTo(groupStudy2Id);
        assertThat(groupStudiesOrderedByMostLikes.get(2).getGroupStudyId()).isEqualTo(groupStudy3Id);
        assertThat(groupStudiesOrderedByMostLikes.size()).isEqualTo(12);

    }

    @DisplayName("Mbti 타입에 맞는 그룹스터디를 최신순으로 반환한다.")
    @Test
    void selectGroupStudyDetailListWithUserMbti() {
        //given
        Mbti mbti = Mbti.Crowded;

        User user = User.builder()
                .email("email")
                .name("name")
                .password("password")
                .nickname("nickNames")
                .role(UserRole.USER)
                .mbti(mbti)
                .build();

        User sameMbtiUser = User.builder()
                .email("email2")
                .name("name2")
                .password("password2")
                .nickname("nickNames2")
                .role(UserRole.USER)
                .mbti(mbti)
                .build();

        User differentMbtiUser = User.builder()
                .email("email3")
                .name("name3")
                .password("password3")
                .nickname("nickNames3")
                .role(UserRole.USER)
                .mbti(Mbti.Timid)
                .build();

        userRepository.save(user);
        userRepository.save(sameMbtiUser);
        userRepository.save(differentMbtiUser);

        GroupStudy userGroupStudy = GroupStudy.builder()
                .userId(user.getUserId())
                .description("description")
                .title("title")
                .session("session")
                .mbti(mbti)
                .build();

        GroupStudy sameMbtiUserGroupStudy = GroupStudy.builder()
                .userId(sameMbtiUser.getUserId())
                .description("description")
                .title("title")
                .session("session")
                .mbti(mbti)
                .build();

        GroupStudy differentMbtiUserGroupStudy = GroupStudy.builder()
                .userId(differentMbtiUser.getUserId())
                .description("description")
                .title("title")
                .session("session")
                .mbti(Mbti.Communication)
                .build();



        groupStudyRepository.save(userGroupStudy);
        groupStudyRepository.save(sameMbtiUserGroupStudy);
        groupStudyRepository.save(differentMbtiUserGroupStudy);

        //when
        List<GroupStudy> groupStudies = groupStudyRepository.selectRecentGroupStudyDetailListWithMbti(mbti, 2);

        //then
        assertThat(groupStudies).hasSize(2);

    }

    private void createThreeGroupStudy() {
        GroupStudy groupStudy1 = GroupStudy.builder()
                .title("title1")
                .description("description")
                .build();

        GroupStudy groupStudy2 = GroupStudy.builder()
                .title("title2")
                .description("description")
                .build();

        GroupStudy groupStudy3 = GroupStudy.builder()
                .title("title3")
                .description("description")
                .build();

        groupStudyRepository.save(groupStudy1);
        groupStudyRepository.save(groupStudy2);
        groupStudyRepository.save(groupStudy3);

    }


    private void oneLikesToGroupStudy3(long groupStudy3Id) {
        GroupStudyLike groupStudy3Like1 = GroupStudyLike.builder()
                .groupStudyId(groupStudy3Id)
                .build();
        groupStudyLikeRepository.save(groupStudy3Like1);
    }

    private void twoLikesToGroupStudy2(long groupStudy2Id) {
        GroupStudyLike groupStudy2Like1 = GroupStudyLike.builder()
                .groupStudyId(groupStudy2Id)
                .build();

        GroupStudyLike groupStudy2Like2 = GroupStudyLike.builder()
                .groupStudyId(groupStudy2Id)
                .build();

        groupStudyLikeRepository.save(groupStudy2Like1);
        groupStudyLikeRepository.save(groupStudy2Like2);

    }

    private void threeLikesToGroupStudy1(long groupStudy1Id) {
        GroupStudyLike groupStudy1Like1 = GroupStudyLike.builder()
                .groupStudyId(groupStudy1Id)
                .build();

        GroupStudyLike groupStudy1Like2 = GroupStudyLike.builder()
                .groupStudyId(groupStudy1Id)
                .build();

        GroupStudyLike groupStudy1Like3 = GroupStudyLike.builder()
                .groupStudyId(groupStudy1Id)
                .build();

        groupStudyLikeRepository.save(groupStudy1Like1);
        groupStudyLikeRepository.save(groupStudy1Like2);
        groupStudyLikeRepository.save(groupStudy1Like3);
    }


}