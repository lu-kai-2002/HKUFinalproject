package com.example.demo.controller;

import com.example.demo.DTO.WordExtractResponse;
import com.example.demo.service.WordService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

    @PostMapping(value = "/upload-word", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<WordExtractResponse>> uploadWord(@RequestPart("file") Mono<FilePart> fileMono) {
        return fileMono.flatMap(filePart -> {
            System.out.println("✅ 文件名: " + filePart.filename());

            // 下面直接调用你的Word解析方法即可
            // 假设你的WordService已经可以处理InputStream类型

            return filePart.content()
                    .reduce(new java.io.ByteArrayOutputStream(), (baos, dataBuffer) -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        baos.write(bytes, 0, bytes.length);
                        return baos;
                    })
                    .map(baos -> {
                        byte[] bytes = baos.toByteArray();
                        String extractedText = ""; // 调用你的 WordService 解析 bytes
                        try {
                            extractedText = WordService.extractText(new java.io.ByteArrayInputStream(bytes));
                            System.out.println("✅ 解析文本: " + extractedText);
                        } catch (Exception e) {
                            extractedText = "解析错误: " + e.getMessage();
                        }
                        return ResponseEntity.ok(new WordExtractResponse(extractedText));
                    });
        }).onErrorResume(e -> {
            System.err.println("❌ 错误：" + e.getMessage());
            return Mono.just(ResponseEntity.status(500)
                    .body(new WordExtractResponse("Error: " + e.getMessage())));
        });
    }
}