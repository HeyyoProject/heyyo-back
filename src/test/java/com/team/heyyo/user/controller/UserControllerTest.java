package com.team.heyyo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.repository.UserRepository;
import com.team.heyyo.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;
    MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    final String USER_API = "/api/users";

    @DisplayName("회원가입 성공")
    @Test
    void registerSuccess() throws Exception {
        //given
        final String api = USER_API + "/register";
        UserRegisterRequest request = UserRegisterRequest.of("name", "test@email.com", "password", "01012341234");
        doReturn(true).when(userService).register(request);
        //when
        final ResultActions resultActions = mockMvc.perform(
                post(api)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("user/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("유저 성함"),
                                fieldWithPath("email").description("유저 이메일"),
                                fieldWithPath("password").description("유저 비밀번호"),
                                fieldWithPath("phoneNumber").description("유저 핸드폰 번호"),
                                fieldWithPath("recommendEmail").ignored()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지")
                        )

                ));

    }

    @DisplayName("로그인에 성공하면 accessToken과 refreshToken을 쿠키에 담아준다.")
    @Test
    void loginSuccess() throws Exception {
        //given
        final String api = USER_API + "/login";
        String email = "email";
        String password = "password";

        User user = User.builder()
                .email(email)
                .password(password)
                .build();
        userRepository.save(user);

        doReturn(user).when(userService).findByEmail(email);
        doReturn(UserResponseCode.SUCCESS).when(userService).isEmailAndPasswordCorrect(email, password);

        //when
        ResultActions resultActions = mockMvc.perform(
                        get(api)
                        .param("email", email)
                        .param("password", password)
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("user/login",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("email").description("유저 이메일"),
                                        parameterWithName("password").description("유저 비밀번호")
                                ),
                                HeaderDocumentation.responseHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseCookies(
                                        cookieWithName("refresh_token").description("refreshToken")
                                )
                        )
                );

    }

} 