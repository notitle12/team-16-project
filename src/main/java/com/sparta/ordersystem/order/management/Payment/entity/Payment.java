package com.sparta.ordersystem.order.management.Payment.entity;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "p_payment")
public class Payment extends Timestamped {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(nullable = false,name ="payment_id")
    private UUID paymentId;

    @OneToOne(cascade = CascadeType.ALL)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name ="payment_status")
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod method;

    /***
     * 결제 내역에 얼마를 결제했는지 정보를 저장하면 좋을거 같아서 추가
     */
    private Integer total_price;

    public void updateStatus(PaymentStatus newStatus){
        this.status = newStatus;
    }
}
