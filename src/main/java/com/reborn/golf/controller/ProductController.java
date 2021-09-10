package com.reborn.golf.controller;


import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.service.ProductService;
import com.reborn.golf.util.MD5Generator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //테스트용 메인 페이지
    @GetMapping("/")
    public String list() {
        return "product/list.html";
    }

    //테스트용 제품 등록 페이지
    @GetMapping("/post")
    public String post() {
        return "product/post.html";
    }

    //쇼핑몰 제품 등록
    @PostMapping("/post")
    public String Write(@RequestParam("file") MultipartFile files, ProductDto productDto) {
        try{
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();

            // 실행되는 위치의 'files' 폴더에 파일이 저장됨
            String savePath = System.getProperty("user.dir") + "\\files";

            // 파일이 저장되는 폴더가 없으면 폴더를 생성함
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            //변경된 file 관련 정보 저장
            productDto.setOrigFilename(origFilename);
            productDto.setFilename(filename);
            productDto.setFilePath(filePath);

            productService.saveFile(productDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
