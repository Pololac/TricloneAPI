package com.hb.cda.examapi.controller.dto.mapper;

import com.hb.cda.examapi.controller.dto.PaymentDTO;
import com.hb.cda.examapi.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "from.id", target = "fromUserId")
    @Mapping(source = "to.id", target = "toUserId")
    @Mapping(source = "account.id", target = "accountId")
    PaymentDTO convertToDTO(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "fromUserId", target = "from.id")
    @Mapping(source = "toUserId", target = "to.id")
    @Mapping(target = "account", ignore = true)
    Payment convertFromPost(PaymentDTO paymentDTO);
}
