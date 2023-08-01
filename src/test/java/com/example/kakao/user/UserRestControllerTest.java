package com.example.kakao.user;

import com.example.kakao.MyRestDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest extends MyRestDoc {

    // ObjectMapper 등록
    @Autowired
    private ObjectMapper om;

    // 정상
    @Test
    public void check_test() throws Exception {
        // given

        // DTO 생성
        UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO();
        requestDTO.setEmail("meta@nate.com");

        // requestBody 만들기
        String requestBody = om.writeValueAsString(requestDTO);
        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/check")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = result.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : " + responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("true"));
        result.andExpect(jsonPath("$.response").value("null"));
        result.andExpect(jsonPath("$.error").value("null"));

    }

    // 에러
    @Test
    public void checkError_test() throws Exception {
        // given
        // DTO 생성
        UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO();
        requestDTO.setEmail("ssarmango@nate.com");

        // requestBody 만들기
        String requestBody = om.writeValueAsString(requestDTO);
        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/check")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = result.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : " + responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error.message").value("동일한 이메일이 존재합니다 : ssarmango@nate.com"));
        result.andExpect(jsonPath("$.error.status").value("400"));
    }


    @Test
    public void join_test() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void login_test() throws Exception {
        // given

        // when

        // then
    }
}
