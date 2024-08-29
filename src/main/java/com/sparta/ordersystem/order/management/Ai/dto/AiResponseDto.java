package com.sparta.ordersystem.order.management.Ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AiResponseDto {

    private UUID ai_id;

    private String text;

}
