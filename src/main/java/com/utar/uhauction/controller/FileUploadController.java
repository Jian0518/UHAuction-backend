package com.utar.uhauction.controller;

import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.service.IImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    @Lazy
    private IImagesService iImagesService;


    @PostMapping("/upload")
    public ApiResult uploadFile(@RequestParam(value = "file",required = false) MultipartFile file){
        if(file.isEmpty()){
            return ApiResult.failed();
        }
        // get the file name
        String OriginalFilename=file.getOriginalFilename();
        // get system time + file suffix to prevent repeated name
        String fileName=System.currentTimeMillis()+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);
        // set storage address
        //1.storage for backend
        String path = "C:\\Users\\Oct23\\OneDrive - Universiti Tunku Abdul Rahman\\Degree\\Y3S1\\UCCC3583 Project 1\\UHAuction\\UHAuction-frontend\\src\\assets\\image\\";
        File dest=new File(path+fileName);
        // check if the file exists
        if(!dest.getParentFile().exists()){
            // create new one if not exists
            dest.getParentFile().mkdirs();
        }
        try {
            // upload backend
            file.transferTo(dest);
            return new ApiResult(200, "File uploaded successfully", fileName);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed();
        }


    }




    @PostMapping("/upload2")
    public ApiResult uploadFile2(@RequestParam(value = "file",required = false) MultipartFile file){
        if(file.isEmpty()){
            return ApiResult.failed();
        }
        String success = "File uploaded successfully!";
        // get the file name
        String OriginalFilename = file.getOriginalFilename();
        // get system time + file suffix to prevent repeated name
        String fileName=System.currentTimeMillis()+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);

        String result = MinioUploadController.uploadImgFile(file,fileName);


        if(success.equals(result)){
            return new ApiResult(200, success, fileName);
        }
        else{
            return ApiResult.failed();
        }

    }

}