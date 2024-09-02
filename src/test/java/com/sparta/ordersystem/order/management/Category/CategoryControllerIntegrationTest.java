package com.sparta.ordersystem.order.management.Category;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryUpdateRequestDto;
import com.sparta.ordersystem.order.management.User.dto.LoginRequestDto;
import com.sparta.ordersystem.order.management.User.dto.SignUpRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryControllerIntegrationTest {

    // mock 가짜 객체
    @Autowired
    private MockMvc mockMvc;

    // json 객체 변환을 위한 객체
    @Autowired
    private ObjectMapper objectMapper;


    // 회원 가입
    @BeforeEach
    void setUp(@Autowired MockMvc mockMvc , @Autowired ObjectMapper objectMapper) throws Exception {
        // 회원 가입 - MASTER
        //given
        SignUpRequestDto masterDto = new SignUpRequestDto();
        masterDto.setUsername("master2");
        masterDto.setPassword("!Password123");
        masterDto.setEmail("master2@test.com");
        masterDto.setMaster(true);
        masterDto.setMasterToken("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC");

        //when - then
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(masterDto)))
                .andExpect(status().isOk());


        // 회원 가입 - OWNER
        //given
        SignUpRequestDto ownerDto = new SignUpRequestDto();
        ownerDto.setUsername("owner2");
        ownerDto.setPassword("!Password123");
        ownerDto.setEmail("owner2@test.com");
        ownerDto.setOwner(true);
        ownerDto.setOwnerToken("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC");

        //when - then
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerDto)))
                .andExpect(status().isOk());


        // 회원 가입 - CUSTOMER
        //given
        SignUpRequestDto customerDto = new SignUpRequestDto();
        customerDto.setUsername("customer2");
        customerDto.setPassword("!Password123");
        customerDto.setEmail("customer2@test.com");

        //when - then
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isOk());


    }

    //로그인 테스트 코드
    private String login(String email, String password) throws Exception {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword(password);


        // when
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jwtToken = result.getResponse().getHeader("Authorization");

        return jwtToken;
    }


    // 카테고리 생성 메서드
    private String createCategory(String jwtToken, CategoryCreateRequestDto createDto) throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/category")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn();

        // 생성된 카테고리의 ID를 JSON 응답에서 추출
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        return jsonNode.get("categoryId").asText();
    }


    @Test
    @Order(1)
    @DisplayName("카테고리 생성 - 요청 값에 빈값 또는 null 값이 들어올 경우 실패")
    void createCategoryBlankCheck() throws Exception {
        //given
        //로그인한 사용자
        String jwtToken = login("master2@test.com","!Password123");
        //생성할 카테고리 컬럼 값
        String categoryName = "";
        //생성할 카테고리 객체
        CategoryCreateRequestDto createDto = new CategoryCreateRequestDto(categoryName);

        // 에러 메시지
        String errorMessage = "categoryName cannot be blank";
        // 에러 코드
        int statusCode = 400;

        // When & Then
        mockMvc.perform(post("/api/v1/category")
                        .header("Authorization", jwtToken) // static 필드 사용
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.statusCode").value(statusCode));

    }


    @Test
    @Order(2)
    @DisplayName("카테고리 수정 - 요청 값에 빈값 또는 null 값이 들어올 경우 실패")
    void updateCategoryBlankCheck() throws Exception {
        //given
        //로그인한 사용자
        String jwtToken = login("master2@test.com","!Password123");
        //생성할 카테고리 값
        String categoryCreateName = "양식";
        //생성할 카테고리 객체
        CategoryCreateRequestDto createDto = new CategoryCreateRequestDto(categoryCreateName);

        //업데이트할 카테고리 ID
        String categoryId = createCategory(jwtToken, createDto);
        //업데이트할 카테고리 값
        String categoryUpdateName ="";
        //업데이트할 카테고리 객체
        CategoryUpdateRequestDto updateDto = new CategoryUpdateRequestDto(categoryUpdateName);

        // 에러 메시지
        String errorMessage = "categoryName cannot be blank";
        // 에러 코드
        int statusCode = 400;

        // When & Then
        mockMvc.perform(patch("/api/v1/category/{category_id}", categoryId)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.statusCode").value(statusCode));

    }



}
