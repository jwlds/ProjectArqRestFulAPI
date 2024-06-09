package com.puccampinas.projectarqrestfulapi.dtos.api;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ListApiResponse<T>(HttpStatus status, String message, List<T> data) {}
