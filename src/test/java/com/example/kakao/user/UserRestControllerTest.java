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
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error").value(nullValue()));

    }

    // 에러 ( 동일한 이메일 존재 )
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

    // 에러 ( 이메일 형식 에러 )
    @Test
    public void checkError2_test() throws Exception {
        // given
        // DTO 생성
        UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO();
        requestDTO.setEmail("ssarnate.com");

        // requestBody 만들기
        String requestBody = om.writeValueAsString(requestDTO);
        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/check")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = result.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error.message").value("이메일 형식으로 작성해주세요:email"));
        result.andExpect(jsonPath("$.error.status").value("400"));
    }

    // 정상
    @Test
    public void join_test() throws Exception {
        // given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setEmail("meta@nate.com");
        requestDTO.setPassword("meta1234!");
        requestDTO.setUsername("ssarmango");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("true"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error").value(nullValue()));
    }

    // 에러 ( email 형식 에러 )
    @Test
    public void joinError_test() throws Exception {
        // given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setEmail("metanate.com");
        requestDTO.setPassword("meta1234!");
        requestDTO.setUsername("ssarmango");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error.message").value("이메일 형식으로 작성해주세요:email"));
        result.andExpect(jsonPath("$.error.status").value("400"));
    }

    // 에러 ( 비밀번호 체크 )
    @Test
    public void joinError2_test() throws Exception {
        // given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setEmail("meta@nate.com");
        requestDTO.setPassword("meta1234");
        requestDTO.setUsername("ssarmango");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"));
        result.andExpect(jsonPath("$.error.status").value("400"));
    }

    // 에러 ( 동일한 이메일 존재 )
    @Test
    public void joinError3_test() throws Exception {
        // given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setEmail("ssarmango@nate.com");
        requestDTO.setPassword("meta1234!");
        requestDTO.setUsername("ssarmango");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error.message").value("동일한 이메일이 존재합니다 : ssarmango@nate.com"));
        result.andExpect(jsonPath("$.error.status").value("400"));
    }

    // 에러 ( 비밀번호 8자 ~ 20자 사이 )
    @Test
    public void joinError4_test() throws Exception {
        // given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setEmail("meta@nate.com");
        requestDTO.setPassword("meta12!");
        requestDTO.setUsername("ssarmango");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.response").value(nullValue()));
        result.andExpect(jsonPath("$.error.message").value("8에서 20자 이내여야 합니다.:password"));
        result.andExpect(jsonPath("$.error.status").value("400"));
    }


    @Test
    public void login_test() throws Exception {
        // given

        // when

        // then
    }
}
