package com.tujuhsembilan.user_service.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tujuhsembilan.user_service.constant.ApiConstant;

@Component
public class ResponseUtil {
    
    public static <T> ResponseEntity<Map<String, Object>> success() {
        return success(null);
    }

    public static <T> ResponseEntity<Map<String, Object>> success(T result) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", ApiConstant.SUCCESS_CODE);
        response.put("message", "Success");
        response.put("result", result);

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Map<String, Object>> error(T result, String customCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", customCode);
        response.put("message", "Error");
        response.put("result", result);

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Map<String, Object>> error(T result, String customCode, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", customCode);
        response.put("message", message);
        response.put("result", result);

        return ResponseEntity.ok(response);
    }
}
