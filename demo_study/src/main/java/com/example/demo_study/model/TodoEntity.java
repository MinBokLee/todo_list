package com.example.demo_study.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoEntity {
    private String id; //TodoEntity ID
    private String userId; // 유저 ID
    private String title;  // 제목
    private boolean done;
}
