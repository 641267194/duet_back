package com.brt.duet.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.brt.duet.util.OSSClientUtil;
import com.brt.duet.util.ResponseUtil;

/**
 * @author 方杰
 * @date 2019年8月10日
 * @description 文件上传示例
 */
@RestController
@RequestMapping("/file")
public class DemoFileController {
	
	@Autowired
	private OSSClientUtil ossClient;
	
	/**
	 * @author 方杰
	 * @date 2019年8月10日
	 * @param file 文件
	 * @return 是否成功
	 * @description 上传一个文件
	 */
	@RequestMapping("/uploadFile")
	public JSONObject testUploadFile(@RequestParam("file") MultipartFile file) {
		JSONObject resultJson = ossClient.uploadFile("test", file);
		if (resultJson.getBoolean("key")) {
			return ResponseUtil.responseResult(true, "成功", resultJson);
		} else {
			return ResponseUtil.responseResult(false, "失败", null);
		}
	}
	
	/**
	 * @author 方杰
	 * @date 2019年8月10日
	 * @param files 多个文件
	 * @return 是否成功
	 * @description 上传多个文件
	 */
	@RequestMapping("/uploadFiles")
	public JSONObject testUploadFiles(@RequestParam("file") MultipartFile[] files) {
		JSONObject resultJson = ossClient.uploadFile("test", files);
		if (resultJson.getBoolean("key")) {
			return ResponseUtil.responseResult(true, "成功", resultJson);
		} else {
			return ResponseUtil.responseResult(false, "失败", null);
		}
	}
	
	/**
	 * @author 方杰
	 * @date 2019年8月10日
	 * @param fileName 文件路径名
	 * @param response
	 * @throws IOException 
	 * @description 下载文件
	 */
	@RequestMapping("/downloadFile")
	public void testDownloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
		OutputStream outputStream = response.getOutputStream();
		ossClient.download(fileName, outputStream);
	}
}
