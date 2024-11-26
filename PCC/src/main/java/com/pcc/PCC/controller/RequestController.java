package com.pcc.PCC.controller;

import com.pcc.PCC.dto.RequestDto;
import com.pcc.PCC.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pcc/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<RequestDto> create(@RequestBody RequestDto requestDto){
        RequestDto request = requestService.create(requestDto);

        return ResponseEntity.ok(request);
    }
}
