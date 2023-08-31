package com.team.heyyo.group.reference.repository.reference;

import com.team.heyyo.config.JpaConfig;
import com.team.heyyo.config.TestConfig;
import com.team.heyyo.group.reference.data.domain.Reference;
import com.team.heyyo.group.reference.data.domain.ReferenceScrap;
import com.team.heyyo.group.reference.repository.referenceScrap.ReferenceScrapRepository;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestConfig.class, JpaConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReferenceRepositoryTest {

    @Autowired
    ReferenceRepository referenceRepository;

    @Autowired
    GroupStudyRepository groupStudyRepository;

    @Autowired
    ReferenceScrapRepository referenceScrapRepository;

    @DisplayName("자료의 오늘 하루 좋아요를 많이 받은 순으로 20개를 리턴한다")
    @Test
    void findReferenceTodayMostLike() {
        //given
        GroupStudy groupStudy = GroupStudy.builder()
                .title("title1")
                .description("description")
                .build();
        groupStudyRepository.save(groupStudy);

        for (int i = 0; i < 22; i++) {
            Reference reference = Reference.builder()
                    .groupStudyId(groupStudy.getGroupStudyId())
                    .title("title")
                    .userId(1L)
                    .build();
            referenceRepository.save(reference);
        }

        //when
        List<Reference> referenceList = referenceRepository.findReferenceTodayMostLike();

        //then
        assertThat(referenceList).hasSize(20);
    }


    @DisplayName("자료를 스크랩 많이 된 순서대로 12개를 리턴한다.")
    @Test
    void getMostScrapReference() {
        //given
        Pageable pageable = PageRequest.of(0, 12);

        GroupStudy groupStudy = GroupStudy.builder()
                .title("title1")
                .description("description")
                .build();
        groupStudyRepository.save(groupStudy);

        Reference reference = Reference.builder()
                .groupStudyId(groupStudy.getGroupStudyId())
                .title("titleBest")
                .userId(1L)
                .build();

        referenceRepository.save(reference);

        for (int i = 1; i < 12; i++) {
            Reference referenceRepeat = Reference.builder()
                    .groupStudyId(groupStudy.getGroupStudyId())
                    .title("title"+i)
                    .userId(1L)
                    .build();
            referenceRepository.save(referenceRepeat);
        }

        for (int i = 0; i < 10; i++) {
            referenceScrapRepository.save(ReferenceScrap.builder()
                    .referenceId(reference.getGroupStudyReferenceId())
                    .build());
        }
        for (int i = 0; i < 3; i++) {
            referenceScrapRepository.save(ReferenceScrap.builder()
                    .referenceId(1L)
                    .build());
        }

        for (int i = 0; i < 2; i++) {
            referenceScrapRepository.save(ReferenceScrap.builder()
                    .referenceId(2L)
                    .build());
        }

        //when
        List<Reference> referenceList = referenceRepository.findAllOrderByScrapDesc(pageable);

        //then
        assertThat(referenceList).hasSize(12);
        assertThat(referenceList.get(0).getTitle()).isEqualTo("titleBest");
        assertThat(referenceList.get(1).getTitle()).isEqualTo("title1");
        assertThat(referenceList.get(2).getTitle()).isEqualTo("title2");
    }

    @DisplayName("자료를 조회수 많은 순서대로 12개를 리턴한다.")
    @Test
    void getMostViewsReference() {
        //given
        Pageable pageable = PageRequest.of(0, 12);

        for (int i = 0; i < 12; i++) {
            Reference reference = Reference.builder()
                    .title("title"+i)
                    .userId(1L)
                    .views(i)
                    .build();
            referenceRepository.save(reference);
        }
        //when
        List<Reference> referenceList = referenceRepository.findAllOrderByViewsDesc(pageable);

        //then
        assertThat(referenceList).hasSize(12);
        assertThat(referenceList.get(0).getViews()).isEqualTo(11);
    }

    @DisplayName("자료를 최근에 생서된 순서대로 12개를 리턴한다.")
    @Test
    void getRecentReference() {
        //given
        Pageable pageable = PageRequest.of(0, 12);

        for (int i = 0; i < 12; i++) {
            Reference reference = Reference.builder()
                    .title("title"+i)
                    .userId(1L)
                    .build();
            referenceRepository.save(reference);
        }
        //when
        List<Reference> referenceList = referenceRepository.findAllOrderByCreatedAtDesc(pageable);

        //then
        assertThat(referenceList).hasSize(12);
        assertThat(referenceList.get(0).getCreatedAt()).isAfter(referenceList.get(1).getCreatedAt());
    }

}