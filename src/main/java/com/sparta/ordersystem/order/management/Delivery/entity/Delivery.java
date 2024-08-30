package com.sparta.ordersystem.order.management.Delivery.entity;

import com.sparta.ordersystem.order.management.Delivery.dto.UpdateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.common.Timestamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_delivery")
public class Delivery extends Timestamped {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( // Hibernate를 사용하여 UUID를 자동으로 생성
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "delivery_Id")
    UUID deliveryId;

    @Size(max = 255)
    @Column(length = 255, nullable = false, unique = true)
    String address;

    @Size(max = 255)
    @Column(length = 255)
    String request_note;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Order order;

    @Column(name = "is_active")
    boolean isActive;

    public void updateDelivery(UpdateDeliveryRequestDto dto){
        this.request_note = dto.getRequest_note();
        this.address = dto.getAddress();
    }

    public void deleteDelivery(Long user_id) {
        this.isActive = false;
        this.deleted_at = LocalDateTime.now();
        this.deleted_by = user_id;
    }

    public boolean getIsActive(){
        return this.isActive;
    }
}
