package com.system.crypto.dto;

import com.system.crypto.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeTrackingDTO {
    String symbolTrade;
    String symbolTarget;
    Double qtyTrade;
    Double qtyTarget;
    Double amountTrade;
    Double amountTarget;
    Date createdDate;
    Date updatedDate;
}
