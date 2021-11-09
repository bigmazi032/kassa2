package com.example.demo.service.dto;

import lombok.Data;

@Data
public class ReceiptItemDto {
    private Long id;
    private Long receiptId;
    private SeatStatusDto seat;
    private Long quantitySeats;
}
