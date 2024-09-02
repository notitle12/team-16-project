package com.sparta.ordersystem.order.management.Ai.entity;

import com.sparta.ordersystem.order.management.User.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_ai")
public class Ai {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(nullable = false,name ="ai_id")
    private UUID ai_id;

    @Column(name = "response", length = 255)
    private String response;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User와의 관계 추가

    public Ai(String response, User user) {
        this.response = response;
        this.user = user;
    }

}
