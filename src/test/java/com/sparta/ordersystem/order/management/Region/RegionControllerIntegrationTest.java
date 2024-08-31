package com.sparta.ordersystem.order.management.Region;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ordersystem.order.management.Region.dto.RegionCreateRequestDto;
import com.sparta.ordersystem.order.management.Region.dto.RegionUpdateRequestDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegionControllerIntegrationTest {

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


    // 지역 생성 메서드
    private String createRegion(String jwtToken, RegionCreateRequestDto createDto) throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn();

        // 생성된 지역의 ID를 JSON 응답에서 추출
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        return jsonNode.get("regionId").asText();
    }


    @Test
    @Order(1)
    @DisplayName("지역 생성 - 요청 값에 빈값 또는 null 값이 들어올 경우 실패")
    void createRegionBlankCheck() throws Exception {
        //given
        //로그인한 사용자
        String jwtToken = login("master2@test.com","!Password123");
        //생성할 지역 컬럼 값
        String regionName = "";
        //생성할 지역 객체
        RegionCreateRequestDto createDto = new RegionCreateRequestDto(regionName);

        // 에러 메시지
        String errorMessage = "regionName cannot be blank";
        // 에러 코드
        int statusCode = 400;

        // When & Then
        mockMvc.perform(post("/api/v1/region")
                        .header("Authorization", jwtToken) // static 필드 사용
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.statusCode").value(statusCode));

    }


    @Test
    @Order(2)
    @DisplayName("지역 수정 - 요청 값에 빈값 또는 null 값이 들어올 경우 실패")
    void updateRegionBlankCheck() throws Exception {
        //given
        //로그인한 사용자
        String jwtToken = login("master2@test.com","!Password123");
        //생성할 지역 값
        String regionCreateName = "분당구";
        //생성할 지역 객체
        RegionCreateRequestDto createDto = new RegionCreateRequestDto(regionCreateName);

        //업데이트할 지역 ID
        String regionId = createRegion(jwtToken, createDto);
        //업데이트할 지역 값
        String regionUpdateName ="";
        //업데이트할 지역 객체
        RegionUpdateRequestDto updateDto = new RegionUpdateRequestDto(regionUpdateName);

        // 에러 메시지
        String errorMessage = "지역명을 반드시 입력해주세요.";
        // 에러 코드
        int statusCode = 400;

        // When & Then
        mockMvc.perform(patch("/api/v1/region/{region_id}", regionId)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.statusCode").value(statusCode));

    }


}
