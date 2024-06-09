package com.puccampinas.projectarqrestfulapi.dtos.api;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(HttpStatus status, String message, T data) {
}
