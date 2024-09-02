package com.sparta.ordersystem.order.management.Ai.controller;

import com.sparta.ordersystem.order.management.Ai.dto.AiRequestDto;
import com.sparta.ordersystem.order.management.Ai.dto.AiResponseDto;
import com.sparta.ordersystem.order.management.Ai.service.AiService;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ai/question")
    public ResponseEntity<AiResponseDto> getRecommendations(
            @RequestBody AiRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Long user_id = userDetails.getUser_id();
            AiResponseDto responseDto = aiService.getRecommendations(requestDto, user_id);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 로그
            System.err.println("Exception occurred in AiController: " + e.getMessage());
            e.printStackTrace();

            // 예외 처리: 적절한 HTTP 상태 코드와 메시지 반환
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
