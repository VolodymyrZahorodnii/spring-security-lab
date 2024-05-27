package ua.kpi.its.lab.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ua.kpi.its.lab.security.dto.ExampleDto

@RestController
class ExampleController {
    @GetMapping("/example")
    fun example(): ExampleDto = ExampleDto("example")
}
// Your code here