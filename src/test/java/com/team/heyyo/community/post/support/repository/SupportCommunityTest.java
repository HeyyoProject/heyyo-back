package com.team.heyyo.community.post.support.repository;

import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;
import com.team.heyyo.community.post.support.community.dto.SupportCommunityResponse;
import com.team.heyyo.community.post.support.community.repository.SupportCommunityRepository;
import com.team.heyyo.community.post.support.tag.domain.SupportCommunityTagData;
import com.team.heyyo.community.post.support.tag.repository.SupportCommunityTagDataRepository;
import com.team.heyyo.config.TestConfig;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SupportCommunityTest {

    @Autowired
    SupportCommunityRepository supportCommunityRepository;

    @Autowired
    SupportCommunityTagDataRepository supportCommunityTagDataRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("게시글 타입으로 검색하기")
    public void findSupportCommunityByType() {
        // given
        User user = userRepository.save(buildUser());
        Pageable pageable = Pageable.ofSize(20 );

        for(int i = 0; i < 2; i++) {
            SupportCommunity supportCommunity = supportCommunityRepository.save(buildCommunity(user));
            SupportCommunityTagData supportCommunityTagData = supportCommunityTagDataRepository.save(buildTagData("tag1"));
            SupportCommunityTagData supportCommunityTagData2 = supportCommunityTagDataRepository.save(buildTagData("tag2"));
            supportCommunity.addTagData(supportCommunityTagData);
            supportCommunity.addTagData(supportCommunityTagData2);
        }

        // when
        List<SupportCommunity> result = supportCommunityRepository.findSupportCommunityResponseBySupportCommunityType(pageable , SupportCommunityType.ANNOUNCEMENT);

        // then
        for(SupportCommunity test : result) {
            assertThat(test.getTagData().size()).isEqualTo(2);
        }
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 타입으로 전체 갯수 검색하기")
    public void findSupportCommunityCountByType() {
        // given
        User user = userRepository.save(buildUser());

        for(int i = 0; i < 2; i++) {
            SupportCommunity supportCommunity = supportCommunityRepository.save(buildCommunity(user));
            SupportCommunityTagData supportCommunityTagData = supportCommunityTagDataRepository.save(buildTagData("tag1"));
            SupportCommunityTagData supportCommunityTagData2 = supportCommunityTagDataRepository.save(buildTagData("tag2"));
            supportCommunity.addTagData(supportCommunityTagData);
            supportCommunity.addTagData(supportCommunityTagData2);
        }

        // when
        long result = supportCommunityRepository.findSupportCommunityResponseCountBySupportCommunityType(SupportCommunityType.ANNOUNCEMENT);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 저장 하기")
    public void savePost() {
        // given
        User user = userRepository.save(buildUser());

        SupportCommunity supportCommunity = buildCommunity(user);
        supportCommunity.addTagData(buildTagData("tag1"));
        supportCommunity.addTagData(buildTagData("tag2"));

        // when
        SupportCommunity result = supportCommunityRepository.save(supportCommunity);

        // then
        assertThat(result.getTagData().size()).isEqualTo(2);
        assertThat(result.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("게시글 아이디로 검색하기")
    public void findSupportCommunityById() {
        // given
        User user = userRepository.save(buildUser());

            SupportCommunity supportCommunity = supportCommunityRepository.save(buildCommunity(user));
            SupportCommunityTagData supportCommunityTagData = supportCommunityTagDataRepository.save(buildTagData("tag1"));
            SupportCommunityTagData supportCommunityTagData2 = supportCommunityTagDataRepository.save(buildTagData("tag2"));
            supportCommunity.addTagData(supportCommunityTagData);
            supportCommunity.addTagData(supportCommunityTagData2);

        // when
        Optional<SupportCommunity> result = supportCommunityRepository.findById(supportCommunity.getSupportCommunityId());

        // then
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("검색어로 게시글 검색하기")
    public void findSupportCommunityResponseBySupportCommunityTypeAndSearch() {
        // given
        User user = userRepository.save(buildUser());
        Pageable pageable = Pageable.ofSize(20);

        SupportCommunity supportCommunity = supportCommunityRepository.save(buildCommunity(user));
        SupportCommunityTagData supportCommunityTagData = supportCommunityTagDataRepository.save(buildTagData("tag1"));
        SupportCommunityTagData supportCommunityTagData2 = supportCommunityTagDataRepository.save(buildTagData("tag2"));
        supportCommunity.addTagData(supportCommunityTagData);
        supportCommunity.addTagData(supportCommunityTagData2);

        // when
        List<SupportCommunity> result = supportCommunityRepository.findSupportCommunityResponseBySupportCommunityTypeAndSearch(pageable , SupportCommunityType.ANNOUNCEMENT , "title");

        // then
        for(SupportCommunity test : result) {
            assertThat(test.getTagData().size()).isEqualTo(2);
        }
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("검색어로 게시글 검색하기 갯수")
    public void findSupportCommunityResponseCountBySupportCommunityTypeAndSearch() {
        // given
        User user = userRepository.save(buildUser());

        SupportCommunity supportCommunity = supportCommunityRepository.save(buildCommunity(user));
        SupportCommunityTagData supportCommunityTagData = supportCommunityTagDataRepository.save(buildTagData("tag1"));
        SupportCommunityTagData supportCommunityTagData2 = supportCommunityTagDataRepository.save(buildTagData("tag2"));
        supportCommunity.addTagData(supportCommunityTagData);
        supportCommunity.addTagData(supportCommunityTagData2);

        // when
        long result = supportCommunityRepository.findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(SupportCommunityType.ANNOUNCEMENT , "title");

        // then
        assertThat(result).isEqualTo(1);
    }

    public User buildUser() {
        return User.builder().email("email").password("password").nickname("nickName").build();
    }

    public SupportCommunityTagData buildTagData(String tag) {
        return SupportCommunityTagData.builder()
                .tag(tag)
                .build();
    }

    public SupportCommunity buildCommunity(User user) {
        return SupportCommunity.builder()
                .title("title")
                .content("content")
                .userId(user.getUserId())
                .supportCommunityType(SupportCommunityType.ANNOUNCEMENT)
                .build();
    }
}
