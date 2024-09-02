package com.sparta.ordersystem.order.management.Region;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ordersystem.order.management.Region.dto.*;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Region.repository.RegionRepository;
import com.sparta.ordersystem.order.management.User.dto.LoginRequestDto;
import com.sparta.ordersystem.order.management.User.dto.SignUpRequestDto;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegionServiceIntegrationTest {

    // mock 가짜 객체
    @Autowired
    private MockMvc mockMvc;

    // json 객체 변환을 위한 객체
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


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


    @Nested
    @DisplayName("지역 생성")
    class CreateRegion{
        @Test
        @DisplayName("MASTER가 지역 생성 요청 : 생성 성공")
        @Order(1)
        void createRegionSuccess() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");
            //생성할 지역 컬럼 값
            String regionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createDto = new RegionCreateRequestDto(regionName);

            User user = userRepository.findByEmail("master2@test.com").get();

            // when - then
            mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto))
                            .header("Authorization",jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(regionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(user.getUser_id()));

        }

        // 지역 생성을 OWNER가 신청했을 때
        @Test
        @DisplayName("OWNER가 지역 생성 요청 : 권한 에러 발생")
        @Order(2)
        void createRegionForbiddenForOwner() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("owner2@test.com","!Password123");
            //생성할 지역 컬럼 값
            String regionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createDto = new RegionCreateRequestDto(regionName);

            // 에러 발생 시 출력되어야 하는 에러 메시지
            /*
                messages.properties 에 등록된 message
                manager.master.possible.action=관리자 또는 마스터 권한이 있어야 {0}을(를) {1} 할 수 있습니다. (현재 권한: {2})
             */
            String expectedMessage = "관리자 또는 마스터 권한이 있어야 지역을(를) 생성 할 수 있습니다. (현재 권한: OWNER)";
            // 에러 발생 시 출력되어야 하는 에러 코드
            int statusCode = 403;


            // when - then
            mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto))
                            .header("Authorization",jwtToken))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorMessage").value(expectedMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));

        }


        // 지역 생성 시 중복된 지역가 존재할 경우
        @Test
        @DisplayName("MASTER가 지역 생성 요청 : 중복된 지역 존재하여 에러 발생")
        @Order(3)
        void createRegionDuplicate() throws Exception{
            //given
            // 로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");

            // 생성할 지역 값
            String regionName = "한식";

            // 생성할 지역 객체
            RegionCreateRequestDto createDto = new RegionCreateRequestDto(regionName);

            // 에러 메시지
            /*
                   messages.properties
                   # 중복 message
                   error.duplicate.item={0} {1}이(가) 중복되었습니다. 다시 확인해 주세요. (현재 {0} {1}: {2})
            */
            String errorMessage = "지역 명이(가) 중복되었습니다. 다시 확인해 주세요. (현재 지역 명: "+regionName+")";

            //에러 코드
            int statusCode = 400;



            //when - then
            //1차 등록
            mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto))
                            .header("Authorization",jwtToken))
                    .andExpect(jsonPath("$.regionName").value(regionName))
                    .andExpect(jsonPath("$.regionId").exists());

            //2차 등록 (중복 발생)
            mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto))
                            .header("Authorization",jwtToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }

    }


    @Nested
    @DisplayName("지역 수정")
    class UpdateRegion{

        @Test
        @DisplayName("MASTER가 지역 수정 요청 : 수정 성공")
        @Order(4)
        void updateRegionSuccess() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");
            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //수정할 지역 ID
            UUID updateRegionId;
            //수정할 지역명
            String updateRegionName = "양식";
            //수정할 지역 객체
            RegionUpdateRequestDto updateReqDto = new RegionUpdateRequestDto(updateRegionName);

            User user = userRepository.findByEmail("master2@test.com").get();


            // when - then
            // 지역 생성
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReqDto))
                            .header("Authorization", jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(user.getUser_id()))
                    .andReturn();

            // 생성된 지역 ID 가져오기
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            RegionUpdateResponseDto updateResDto = objectMapper.readValue(jsonResponse, RegionUpdateResponseDto.class);
            updateRegionId = updateResDto.getRegionId();

            // 지역 수정
            mockMvc.perform(patch("/api/v1/region/{region_id}",updateRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReqDto))
                            .header("Authorization",jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(updateRegionName))
                    .andExpect(jsonPath("$.regionId").value(updateRegionId.toString()))
                    .andExpect(jsonPath("$.updatedAt").exists())
                    .andExpect(jsonPath("$.updatedBy").value(user.getUser_id()));



        }


        @Test
        @DisplayName("OWNER가 지역 수정 요청 : 권한 에러 발생")
        @Order(5)
        void updateRegionForbiddenForOwner() throws Exception{
            //given
            //로그인한 사용자- owner
            String jwtOwnerToken = login("owner2@test.com","!Password123");

            //로그인한 사용자 - master
            String jwtMasterToken = login("master2@test.com","!Password123");

            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //수정할 지역 ID
            UUID updateRegionId;
            //수정할 지역명
            String updateRegionName = "양식";
            //수정할 지역 객체
            RegionUpdateRequestDto updateReqDto = new RegionUpdateRequestDto(updateRegionName);


            // 에러 발생 시 출력되어야 하는 에러 메시지
            /*
                messages.properties 에 등록된 message
                manager.master.possible.action=관리자 또는 마스터 권한이 있어야 {0}을(를) {1} 할 수 있습니다. (현재 권한: {2})
             */
            String errorMessage = "관리자 또는 마스터 권한이 있어야 지역을(를) 수정 할 수 있습니다. (현재 권한: OWNER)";
            // 에러 발생 시 출력되어야 하는 에러 코드
            int statusCode = 403;

            User masterUser = userRepository.findByEmail("master2@test.com").get();

            // when - then
            // 지역 생성
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReqDto))
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                    .andReturn();

            // 생성된 지역 ID 가져오기
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            RegionUpdateResponseDto updateResDto = objectMapper.readValue(jsonResponse, RegionUpdateResponseDto.class);
            updateRegionId = updateResDto.getRegionId();


            // 지역 수정
            mockMvc.perform(patch("/api/v1/region/{region_id}",updateRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReqDto))
                            .header("Authorization",jwtOwnerToken))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));

        }



        @Test
        @DisplayName("MASTER가 지역 수정 요청 : 중복된 지역 존재하여 에러 발생")
        @Order(6)
        void updateRegionDuplicate() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");
            //생성할 지역명 -1
            String oneCreateRegionName = "한식";
            //생성할 지역 객체 -1
            RegionCreateRequestDto oneCreateReqDto = new RegionCreateRequestDto(oneCreateRegionName);

            //생성할 지역명 -2
            String twoCreateRegionName = "양식";
            //생성할 지역 객체 -2
            RegionCreateRequestDto twoCreateReqDto = new RegionCreateRequestDto(twoCreateRegionName);

            //"양식" -> "한식"
            //수정할 지역 ID
            UUID updateRegionId;
            //수정할 지역명
            String updateRegionName = "한식";
            //수정할 지역 객체
            RegionUpdateRequestDto updateReqDto = new RegionUpdateRequestDto(updateRegionName);

            // 에러 메시지
            /*
                   messages.properties
                   # 중복 message
                   error.duplicate.item={0} {1}이(가) 중복되었습니다. 다시 확인해 주세요. (현재 {0} {1}: {2})
            */
            String errorMessage = "지역 명이(가) 중복되었습니다. 다시 확인해 주세요. (현재 지역 명: "+updateRegionName+")";

            //에러 코드
            int statusCode = 400;

            User masterUser = userRepository.findByEmail("master2@test.com").get();


            //when - then
            // 등록 - 한식
            mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(oneCreateReqDto))
                            .header("Authorization",jwtToken))
                    .andExpect(jsonPath("$.regionName").value(oneCreateRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()));

            // 등록 - 양식
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(twoCreateReqDto))
                            .header("Authorization", jwtToken))
                    .andExpect(jsonPath("$.regionName").value(twoCreateRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                    .andReturn();


            // 생성된 지역 ID 가져오기
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            RegionCreateResponseDto createResDto = objectMapper.readValue(jsonResponse, RegionCreateResponseDto.class);
            updateRegionId = createResDto.getRegionId();


            // 지역 수정
            mockMvc.perform(patch("/api/v1/region/{region_id}",updateRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReqDto))
                            .header("Authorization",jwtToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }



        @Test
        @DisplayName("MASTER가 지역 수정 요청 : DB에 존재하지 않는 ID로 수정 요청 ")
        @Order(7)
        void updateRegionNullRegionId() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");
            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //수정할 지역 ID (임의 생성)
            UUID updateRegionId = UUID.randomUUID();
            //수정할 지역명
            String updateRegionName = "양식";
            //수정할 지역 객체
            RegionUpdateRequestDto updateReqDto = new RegionUpdateRequestDto(updateRegionName);

            // 에러 메시지
            /*
                messages.properties
                error.null.item= {0} {1}(으)로 {0}(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 {0} {1}: {2})
            */
            String errorMessage= "지역 ID(으)로 지역(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 지역 ID: "+updateRegionId+")";
            // 에러 코드
            int statusCode = 404;


            // when - then
            // 임의의 ID로 지역 수정
            mockMvc.perform(patch("/api/v1/region/{region_id}",updateRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReqDto))
                            .header("Authorization",jwtToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }
    }


    @Nested
    @DisplayName("지역 삭제")
    class Deleteategory{

        @Test
        @DisplayName("MASTER가 지역 삭제 요청 : 삭제 성공")
        @Order(8)
        void deleteRegionSuccess() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");
            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //삭제할 지역 ID
            UUID deleteRegionId;

            User masterUser = userRepository.findByEmail("master2@test.com").get();


            // when - then
            // 지역 생성
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReqDto))
                            .header("Authorization", jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                    .andReturn();

            // 생성된 지역 ID 가져오기
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            RegionCreateResponseDto createResDto = objectMapper.readValue(jsonResponse, RegionCreateResponseDto.class);
            deleteRegionId = createResDto.getRegionId();

            // 지역 삭제
            mockMvc.perform(delete("/api/v1/region/{region_id}", deleteRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").value(deleteRegionId.toString()))
                    .andExpect(jsonPath("$.active").value(false))
                    .andExpect(jsonPath("$.deletedAt").exists())
                    .andExpect(jsonPath("$.deletedBy").value(masterUser.getUser_id()));;
        }



        @Test
        @DisplayName("CUSTOMER가 지역 삭제 요청 : 권한 에러 발생")
        @Order(9)
        void deleteRegionForbiddenForOwner() throws Exception{
            //given
            //로그인한 사용자- customer
            String jwtCustomerToken = login("customer2@test.com","!Password123");

            //로그인한 사용자 - master
            String jwtMasterToken = login("master2@test.com","!Password123");

            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //삭제할 지역 ID
            UUID deleteRegionId;

            User masterUser = userRepository.findByEmail("master2@test.com").get();

            // 에러 발생 시 출력되어야 하는 에러 메시지
            /*
                messages.properties 에 등록된 message
                manager.master.possible.action=관리자 또는 마스터 권한이 있어야 {0}을(를) {1} 할 수 있습니다. (현재 권한: {2})
             */
            String errorMessage = "관리자 또는 마스터 권한이 있어야 지역을(를) 삭제 할 수 있습니다. (현재 권한: CUSTOMER)";
            // 에러 발생 시 출력되어야 하는 에러 코드
            int statusCode = 403;


            // when - then
            // 지역 생성
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReqDto))
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                    .andReturn();

            // 생성된 지역 ID 가져오기
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            RegionCreateResponseDto createResDto = objectMapper.readValue(jsonResponse, RegionCreateResponseDto.class);
            deleteRegionId = createResDto.getRegionId();

            // 지역 삭제
            mockMvc.perform(delete("/api/v1/region/{region_id}", deleteRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtCustomerToken))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }




        @Test
        @DisplayName("MASTER가 지역 삭제 요청 : DB에 존재하지 않는 ID로 삭제 요청 ")
        @Order(10)
        void deleteRegionNullRegionId() throws Exception{
            //given
            //로그인한 사용자
            String jwtToken = login("master2@test.com","!Password123");
            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //삭제할 지역 ID (임의 생성)
            UUID deleteRegionId = UUID.randomUUID();

            // 에러 메시지
            /*
                messages.properties
                error.null.item= {0} {1}(으)로 {0}(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 {0} {1}: {2})
            */
            String errorMessage= "지역 ID(으)로 지역(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 지역 ID: "+deleteRegionId+")";
            // 에러 코드
            int statusCode = 404;


            // when - then
            // 임의의 ID로 지역 삭제
            mockMvc.perform(delete("/api/v1/region/{region_id}",deleteRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }


   /*     @Test
        @DisplayName("MASTER가 지역 삭제 요청 : 지역 삭제했을 때 연관된 가게도 삭제되어야 함 ")
        @Order(11)
        void deleteRegionCascadeStore() throws Exception{

        }*/

    }


    @Nested
    @DisplayName("지역 조회")
    class GetRegion{

        @Test
        @DisplayName("모든 사용자가 지역 단건 조회 요청 : 조회 성공")
        @Order(11)
        void getRegionSuccess() throws Exception{
            //given
            //로그인한 사용자 - master
            String jwtMasterToken = login("master2@test.com","!Password123");
            //로그인한 사용자 - owner
            String jwtOwnerToken = login("owner2@test.com","!Password123");
            //로그인한 사용자 - customer
            String jwtCustomerToken = login("customer2@test.com","!Password123");

            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //조회할 지역 ID
            UUID getRegionId;

            User masterUser = userRepository.findByEmail("master2@test.com").get();

            // when - then
            // 지역 생성
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReqDto))
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                    .andReturn();

            // 생성된 지역 ID 가져오기
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            RegionCreateResponseDto createResDto = objectMapper.readValue(jsonResponse, RegionCreateResponseDto.class);
            getRegionId = createResDto.getRegionId();

            // 지역 단건 조회 - Master
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").value(getRegionId.toString()));

            // 지역 단건 조회 - Owner
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtOwnerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").value(getRegionId.toString()));

            // 지역 단건 조회 - Customer
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtCustomerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").value(getRegionId.toString()));
        }




        @Test
        @DisplayName("모든 사용자가 지역 전체 조회 요청 : 조회 성공 ")
        @Order(12)
        void getRegionListSuccess() throws Exception{
            //given
            String jwtMasterToken = login("master2@test.com","!Password123");        //로그인한 사용자 - master
            String jwtOwnerToken = login("owner2@test.com","!Password123");          //로그인한 사용자 - owner
            String jwtCustomerToken = login("customer2@test.com","!Password123");    //로그인한 사용자 - customer

            User masterUser = userRepository.findByEmail("master2@test.com").get();

            //생성할 지역명 배열
            String[] createRegionNameArr = {"한식","양식","중식"};
            int createRegionLength = createRegionNameArr.length;

            //비교할 지역명 배열 (지역명 기준 오름차순)
            String[] sortedArr = createRegionNameArr.clone();
            Arrays.sort(sortedArr);

            //생성할 지역 객체 배열
            RegionCreateRequestDto[] createReqDtoArr = new RegionCreateRequestDto[createRegionLength];
            for(int i =0; i< createRegionLength; i++){
                createReqDtoArr[i] = new RegionCreateRequestDto(createRegionNameArr[i]);
            }

            // Request Param
            int page = 1; // 페이지 번호 (0부터 시작)
            int size = 5; // 페이지 당 아이템 수
            String sortBy = "regionName"; // 정렬 기준
            boolean isAsc = true; // 오름차순 정렬


            // when - then

            // 지역 생성
            for(int i =0; i< createRegionLength; i++) {
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/region")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createReqDtoArr[i]))
                                .header("Authorization", jwtMasterToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.regionName").value(createRegionNameArr[i]))
                        .andExpect(jsonPath("$.regionId").exists())
                        .andExpect(jsonPath("$.createdAt").exists())
                        .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                        .andReturn();
            }

            // 지역 전체 조회 - Master
            mockMvc.perform(get("/api/v1/region")
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .param("sortBy", sortBy)
                            .param("isAsc", String.valueOf(isAsc))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(createRegionLength))
                    .andExpect(jsonPath("$[0].regionName").value(sortedArr[0]))
                    .andExpect(jsonPath("$[1].regionName").value(sortedArr[1]))
                    .andExpect(jsonPath("$[2].regionName").value(sortedArr[2]));

            // 지역 전체 조회 - Owner
            mockMvc.perform(get("/api/v1/region")
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .param("sortBy", sortBy)
                            .param("isAsc", String.valueOf(isAsc))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtOwnerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(createRegionLength))
                    .andExpect(jsonPath("$[0].regionName").value(sortedArr[0]))
                    .andExpect(jsonPath("$[1].regionName").value(sortedArr[1]))
                    .andExpect(jsonPath("$[2].regionName").value(sortedArr[2]));


            // 지역 전체 조회 - Customer
            mockMvc.perform(get("/api/v1/region")
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .param("sortBy", sortBy)
                            .param("isAsc", String.valueOf(isAsc))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtCustomerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(createRegionLength))
                    .andExpect(jsonPath("$[0].regionName").value(sortedArr[0]))
                    .andExpect(jsonPath("$[1].regionName").value(sortedArr[1]))
                    .andExpect(jsonPath("$[2].regionName").value(sortedArr[2]));
        }



        @DisplayName("모든 사용자가 지역 단건 조회 요청 : DB에 존재하지 않는 ID로 조회 요청 ")
        @Order(13)
        @Test
        void getRegionNullRegionId() throws Exception{
            //given
            String jwtMasterToken = login("master2@test.com","!Password123");        //로그인한 사용자 - master
            String jwtOwnerToken = login("owner2@test.com","!Password123");          //로그인한 사용자 - owner
            String jwtCustomerToken = login("customer2@test.com","!Password123");    //로그인한 사용자 - customer

            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            //조회할 지역 ID (임의 생성)
            UUID getRegionId = UUID.randomUUID();

            // 에러 메시지
            /*
                messages.properties
                error.null.item= {0} {1}(으)로 {0}(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 {0} {1}: {2})
            */
            String errorMessage= "지역 ID(으)로 지역(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 지역 ID: "+ getRegionId +")";
            // 에러 코드
            int statusCode = 404;


            // when - then
            // 임의의 ID로 지역 조회 - master
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtMasterToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));

            // 임의의 ID로 지역 조회 - owner
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtOwnerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));

            // 임의의 ID로 지역 조회 - customer
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtCustomerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }


        @DisplayName("모든 사용자가 지역 단건 조회 요청 : 삭제된 RegionId로 조회 요청 ")
        @Order(14)
        @Test
        void getRegionAlreadyDeletedRegionId() throws Exception{
            //given
            String jwtMasterToken = login("master2@test.com","!Password123");        //로그인한 사용자 - master
            String jwtOwnerToken = login("owner2@test.com","!Password123");          //로그인한 사용자 - owner
            String jwtCustomerToken = login("customer2@test.com","!Password123");    //로그인한 사용자 - customer

            //생성할 지역명
            String createRegionName = "한식";
            //생성할 지역 객체
            RegionCreateRequestDto createReqDto = new RegionCreateRequestDto(createRegionName);

            UUID getRegionId;             //조회할 지역 ID
            UUID deleteRegionId;            //삭제할 지역 ID

            // 에러 메시지
            /*
                messages.properties
                error.null.item= {0} {1}(으)로 {0}(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 {0} {1}: {2})
            */
            String errorMessage;
            // 에러 코드
            int statusCode = 404;

            User masterUser = userRepository.findByEmail("master2@test.com").get();


            // when - then
            // 지역 생성
            MvcResult mvcCreateResult = mockMvc.perform(post("/api/v1/region")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReqDto))
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.createdBy").value(masterUser.getUser_id()))
                    .andReturn();


            // 생성된 지역 ID 가져오기
            String jsonCreateResponse = mvcCreateResult.getResponse().getContentAsString();
            RegionCreateResponseDto createResDto = objectMapper.readValue(jsonCreateResponse, RegionCreateResponseDto.class);
            deleteRegionId = createResDto.getRegionId();


            // 지역 삭제
            MvcResult mvcDeleteResult = mockMvc.perform(delete("/api/v1/region/{region_id}", deleteRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", jwtMasterToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionName").value(createRegionName))
                    .andExpect(jsonPath("$.regionId").value(deleteRegionId.toString()))
                    .andExpect(jsonPath("$.active").value(false))
                    .andExpect(jsonPath("$.deletedAt").exists())
                    .andExpect(jsonPath("$.deletedBy").value(masterUser.getUser_id()))
                    .andReturn();

            // 삭제된 지역 ID 가져오기
            String jsonDeleteResponse = mvcDeleteResult.getResponse().getContentAsString();
            RegionDeleteResponseDto deleteResDto = objectMapper.readValue(jsonDeleteResponse, RegionDeleteResponseDto.class);
            getRegionId = deleteResDto.getRegionId();


            // when - then
            errorMessage= "지역 ID(으)로 지역(을)를 조회하려 했으나 해당 항목이 존재하지 않습니다. (현재 지역 ID: "+ getRegionId +")";
            // 삭제된 ID로 지역 조회 - master
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtMasterToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));



            // 삭제된 ID로 지역 조회 - owner
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtOwnerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));



            // 삭제된 ID로 지역 조회 - customer
            mockMvc.perform(get("/api/v1/region/{region_id}", getRegionId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization",jwtCustomerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(statusCode));
        }
    }






}