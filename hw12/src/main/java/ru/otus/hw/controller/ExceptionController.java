package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ExceptionController {

    @GetMapping("/forbidden")
    public String forbidden() {
        return "forbidden";
    }

}
