package com.system.crypto.dto;

import lombok.Data;

@Data
public class BaseResponseDTO<T> {
    T data;
    String status;
    Long ts;
}
