package com.team.heyyo.group.study.repository.groupstudytag;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.group.study.domain.GroupStudyTag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupStudyTagRepositoryTest {

    @Autowired
    GroupStudyTagRepository groupStudyTagRepository;

    @BeforeEach
    void setUp() {
        groupStudyTagRepository.deleteAll();
    }

    @DisplayName("최근에 생성된 그룹스터디의 리스트를 반환한다.")
    @Test
    void selectRecentGroupStudies() {
        //given
        final Long groupStudyId = 1L;
        String tag1 = "tag1";
        String tag2 = "tag2";
        String tag3 = "tag3";

        groupStudyTagRepository.save(createGroupStudyTag(groupStudyId, tag1));
        groupStudyTagRepository.save(createGroupStudyTag(groupStudyId, tag2));
        groupStudyTagRepository.save(createGroupStudyTag(groupStudyId, tag3));

        //when
        List<String> tagDataList = groupStudyTagRepository.findByTagDataByGroupStudyId(1L);

        //then
        assertThat(tagDataList).hasSize(3);
        assertThat(tagDataList.get(0)).isEqualTo(tag1);
        assertThat(tagDataList.get(1)).isEqualTo(tag2);
        assertThat(tagDataList.get(2)).isEqualTo(tag3);

    }

    private GroupStudyTag createGroupStudyTag(Long groupStudyId, String tagData) {
        return GroupStudyTag.builder()
                .groupStudyId(groupStudyId)
                .tagData(tagData)
                .build();
    }

}