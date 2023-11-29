package com.example.todo.todoapi.dto.request;

import com.example.todo.todoapi.entity.Todo;
import com.example.todo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoCreateRequestDTO {


    @NotBlank
    @Size(min = 2, max = 30)
    private String title; // 타이틀이 올 것이다

    // dto를 엔터티로 변환하는 메서드
    public Todo toEntity(User user) {
        return Todo.builder()
                .title(this.title)
                .user(user)
                .build();
    }




}
