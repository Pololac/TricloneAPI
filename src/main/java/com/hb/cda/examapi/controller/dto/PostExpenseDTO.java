package com.hb.cda.examapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class PostExpenseDTO {
    @NotBlank
    private String description;
    @NotNull
    @Positive
    private Double amount;
    @NotBlank
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Le payerId doit être un UUID valide"
    )
    private String payerId;
    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "Europe/Paris")
    private LocalDateTime date;

    public PostExpenseDTO() {}

    public PostExpenseDTO(String description, Double amount, String payerId, LocalDateTime date) {
        this.description = description;
        this.amount = amount;
        this.payerId = payerId;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
