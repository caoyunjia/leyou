package com.leyou.upload.web;

import com.leyou.upload.serivce.UploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("image")
    @ApiOperation(value = "图片上传接口", notes = "上传图片后返回图片url")
    public ResponseEntity<String> uploadImage(MultipartFile file, HttpServletRequest request){

        String url=uploadService.uploadImage(file,request);

        return ResponseEntity.ok(url);

    }

}
