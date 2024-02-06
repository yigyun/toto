package com.main.toto.auction.controller;

import com.main.toto.auction.dto.upload.UploadFileDTO;
import com.main.toto.auction.dto.upload.UploadResultDTO;
import com.main.toto.config.ControllerAuctionTestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileControllerTest extends ControllerAuctionTestConfig {

    @SpyBean
    FileController fileController;

    @DisplayName("파일 업로드 테스트")
    @Test
    void upload() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setFiles(Arrays.asList(file, file2));
        //when
        List<UploadResultDTO> result = fileController.upload(uploadFileDTO);
        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test.txt", result.get(0).getFileName());
    }

    @DisplayName("파일 다운로드 테스트")
    @Test
    void viewFileGET() {
        //given
        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setFiles(Arrays.asList(file));
        List<UploadResultDTO> result = fileController.upload(uploadFileDTO);
        String fileName = result.get(0).getUuid() + "_" + result.get(0).getFileName();
        //when
        ResponseEntity<Resource> responseEntity = fileController.viewFileGET(fileName);
        //then
        assertNotNull(responseEntity);
        assertEquals(fileName, responseEntity.getBody().getFilename());
    }

    @DisplayName("파일 삭제 테스트")
    @Test
    void removeFile() {
        //given
        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setFiles(Arrays.asList(file));
        List<UploadResultDTO> result = fileController.upload(uploadFileDTO);
        //when
        Map<String, Boolean> response = fileController.removeFile(result.get(0).getUuid() + "_" + result.get(0).getFileName());
        //then
        assertNotNull(response);
        assertTrue(response.get("result"));
    }

}