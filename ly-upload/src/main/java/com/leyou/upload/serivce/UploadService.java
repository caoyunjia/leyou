package com.leyou.upload.serivce;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.FTPUtil;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    @Autowired
    private UploadProperties props;


    public String uploadImage(MultipartFile file, HttpServletRequest request) {
        try {
            String contentType = file.getContentType();
            if (!props.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnums.INVALID_FILE_ERROR);
            }
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnums.INVALID_FILE_ERROR);
            }
            //获取文件后缀名
            String name = file.getName();
            String originalFilename = file.getOriginalFilename();
            String fileExtensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");


            String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;

            String path = request.getSession().getServletContext().getRealPath("/");
            File targetFile = new File(path+File.separator+uploadFileName);
            file.transferTo(targetFile);
            FTPUtil.uploadFile(props.getFtp(),Arrays.asList(targetFile));
            targetFile.delete();
            return props.getBaseUrl()+File.separator+uploadFileName;
        } catch (IOException e) {
            log.error("文件上传失败", e.getMessage());
            throw new LyException(ExceptionEnums.UPLOAD_FILE_ERROR);
        }
    }
}
