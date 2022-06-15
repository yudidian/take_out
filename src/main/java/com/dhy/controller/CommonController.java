package com.dhy.controller;

import com.dhy.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@ResponseBody
public class CommonController {

  @Value("${kola.path}")
  private String basePath;

  @PostMapping("/upload")
  public R<String> upload(MultipartFile file){
    String originalFilename = file.getOriginalFilename();
    String name = UUID.randomUUID().toString();
    assert originalFilename != null;
    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
    String fileName = name+suffix;
    File dir = new File(basePath);
    if (!dir.exists()){
      dir.mkdirs();
    }
    try {
      file.transferTo(new File(basePath+fileName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return R.success(null,fileName);
  }
  @GetMapping("/download")
  public void download(String fileName, HttpServletResponse response){
    try {
      FileInputStream fileInputStream = new FileInputStream(new File(basePath + fileName));
      ServletOutputStream outputStream = response.getOutputStream();
      response.setContentType("image/jpeg");
      int len = 0;
      byte[] bytes = new byte[1024];
      while ((len = fileInputStream.read(bytes)) != -1){
        outputStream.write(bytes,0,len);
        outputStream.flush();
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
