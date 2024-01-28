package com.main.toto.auction.controller;


import com.main.toto.auction.dto.upload.UploadFileDTO;
import com.main.toto.auction.dto.upload.UploadResultDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class FileController {

    @Value("${com.main.upload.path}")
    private String uploadPath;


    @ApiOperation(value = "Upload Post", notes = "Post 방식으로 파일 등록하기")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null){

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

                boolean image = false;


                try{
                    multipartFile.transferTo(savePath);

                    if(Files.probeContentType(savePath).startsWith("image")){
                        image = true;
                        File thumbFile = new File(uploadPath, "s_"+uuid+"_"+originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                } catch (IOException e ){
                    throw new CustomIOException("파일 저장에 실패했습니다", e);
                }

                list.add(UploadResultDTO
                        .builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .img(image)
                        .build());
            });

            return list;
        }

        return null;
    }


    @ApiOperation(value = "view 파일", notes = "Get으로 파일을 조회한다.")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        log.info("fileName: " + fileName);

        Resource resource;
        try {
            resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 파일 경로입니다.", e);
        }
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value = "remove 파일", notes = "DELETE 방식으로 파일을 삭제한다.")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){

        log.info("remove 파일: fileName = " + fileName);
        Resource resource;

        try {
            resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("유효하지 않은 파일 경로입니다.");
        }

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        log.info("remove 파일: fileName = " + fileName);

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                if(!thumbnailFile.delete())
                    log.error("delete file error");
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }

    // 500 코드 처리
    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<String> handleCustomIOException(CustomIOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("파일 저장에 실패했습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // Log the exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("유효하지 않은 파일 경로입니다.");
    }

    private class CustomIOException extends RuntimeException {
        public CustomIOException(String message, IOException e) {
            super(message, e);
        }
    }
}
