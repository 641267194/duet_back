package com.brt.duet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

/**
 * @author 方杰
 * @date 2019年1月9日
 * @description 阿里OSS工具类
 */
public class OSSClientUtil {
	
	/**
	 * @description 地域节点
	 */
	private String endpoint;
	/**
	 * @description 阿里云RAM访问控制accessKeyId
	 */
	private String accessKeyId;
	/**
	 * @description 阿里云RAM访问控制accessKeySecret
	 */
	private String accessKeySecret;
	/**
	 * @description 存储空间bucket名称
	 */
	private String bucket;
	/**
	 * @description 请求url前缀
	 */
	private String url;
	/**
	 * @description 最大文件大小
	 */
	private String filesize;
	/**
	 * @description 最大图片大小
	 */
	private String imagesize;
	
	public OSSClientUtil() {
		super();
	}
	
	public OSSClientUtil(
			String endpoint, 
			String accessKeyId, 
			String accessKeySecret, 
			String bucket, 
			String url,
			String filesize, 
			String imagesize) {
		super();
		this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.bucket = bucket;
		this.url = url;
		this.filesize = filesize;
		this.imagesize = imagesize;
	}
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getImagesize() {
		return imagesize;
	}

	public void setImagesize(String imagesize) {
		this.imagesize = imagesize;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadPath 上传路径
	 * @param uploadFile 需要上传的文件
	 * @return 是否成功+文件信息
	 * @description 上传一个文件
	 */
	public JSONObject uploadFile(String uploadPath, MultipartFile uploadFile) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", false);
	    if (!validateFileSize(uploadFile)) {
	    	jsonObject.put("value", "上传文件不能大于"+Long.parseLong(filesize)/1024/1024+"MB");
	    	return jsonObject;
		}
	    if (!validateFileType(uploadFile)) {
	    	jsonObject.put("value", "上传文件格式错误");
	    	return jsonObject;
		}
	    String ext = getFileExt(uploadFile);
	    OSS ossClient = null;
	    try {
	    	ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			String path = uploadPath+"/"+DateUtil.getYmd();
			String filename = uploadFile.getOriginalFilename();
			String realPath = path+IDUtils.getUUID()+"."+ext;
			ObjectMetadata obj = new ObjectMetadata();
			obj.setContentDisposition("attachment;filename=\"" + filename + "\"");
	    	ossClient.putObject(bucket, realPath, uploadFile.getInputStream(), obj);
	    	/*// 创建上传文件元信息。
	    	ObjectMetadata metadata = new ObjectMetadata();
	    	metadata.setContentType("text/plain");
	    	// 设置自定义元信息property的值为property-value。
	    	metadata.addUserMetadata("property", "property-value");
	    	// 创建CreateSymlinkRequest。
	    	String softPath = uploadPath+"/softPath/"+IDUtils.getUUID()+"/"+filename;
	    	CreateSymlinkRequest createSymlinkRequest = new CreateSymlinkRequest(bucket, softPath, realPath);
	    	// 设置元信息。
	    	createSymlinkRequest.setMetadata(metadata);
	    	// 创建软链接。
	    	ossClient.createSymlink(createSymlinkRequest);*/
	    	JSONObject json = new JSONObject();
    		json.put("filename", filename);
    		json.put("url", url);
    		json.put("path", realPath);
    		jsonObject.put("key", true);
    		jsonObject.put("value", json);
		} catch (Exception e) {
			jsonObject.put("value", "上传失败");
			e.getMessage();
		} finally {
			ossClient.shutdown();
		}
		return jsonObject;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadPath 上传路径
	 * @param uploadFiles 需要上传的文件集合
	 * @return 是否成功+文件信息集合
	 * @description 
	 */
	public JSONObject uploadFile(String uploadPath, MultipartFile[] uploadFiles) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", false);
		for (MultipartFile uploadFile : uploadFiles) {
			if (!validateFileSize(uploadFile)) {
		    	jsonObject.put("value", "上传文件不能大于"+Long.parseLong(filesize)/1024/1024+"MB");
		    	return jsonObject;
			}
		    if (!validateFileType(uploadFile)) {
		    	jsonObject.put("value", "上传文件格式错误");
		    	return jsonObject;
			}
		}
	    OSS ossClient = null;
	    try {
	    	ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	    	List<JSONObject> list = new ArrayList<JSONObject>();
	    	JSONObject json = new JSONObject();
	    	for (MultipartFile uploadFile : uploadFiles) {
	    		String ext = getFileExt(uploadFile);
	    		String path = uploadPath+"/"+DateUtil.getYmd();
				String filename = uploadFile.getOriginalFilename();
				String realPath = path+IDUtils.getUUID()+"."+ext;
				ObjectMetadata obj = new ObjectMetadata();
				obj.setContentDisposition("attachment;filename=\"" + filename + "\"");
		    	ossClient.putObject(bucket, realPath, uploadFile.getInputStream(), obj);
	    		json = new JSONObject();
	    		json.put("filename", filename);
	    		json.put("url", url);
	    		json.put("path", realPath);
	    		list.add(json);
			}
			jsonObject.put("key", true);
			jsonObject.put("value", list);
		} catch (Exception e) {
			jsonObject.put("value", "上传失败");
			e.getMessage();
		} finally {
			ossClient.shutdown();
		}
		return jsonObject;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadPath 上传路径
	 * @param uploadFile 需要上传的图片
	 * @return 是否成功+文件信息
	 * @description 上传一张图片
	 */
	public JSONObject uploadImage(String uploadPath, MultipartFile uploadFile) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", false);
	    if (!validateImageSize(uploadFile)) {
	    	jsonObject.put("value", "上传图片不能大于"+Long.parseLong(imagesize)/1024/1024+"MB");
	    	return jsonObject;
		}
	    if (!validateImageType(uploadFile)) {
	    	jsonObject.put("value", "上传图片格式错误");
	    	return jsonObject;
		}
	    String ext = getFileExt(uploadFile);
	    OSS ossClient = null;
	    try {
	    	ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	    	String path = uploadPath+"/"+DateUtil.getYmd();
			String filename = uploadFile.getOriginalFilename();
			String realPath = path+IDUtils.getUUID()+"."+ext;
			ObjectMetadata obj = new ObjectMetadata();
			obj.setContentDisposition("attachment;filename=\"" + filename + "\"");
	    	ossClient.putObject(bucket, realPath, uploadFile.getInputStream(), obj);
	    	JSONObject json = new JSONObject();
    		json.put("filename", filename);
    		json.put("url", url);
    		json.put("path", realPath);
    		jsonObject.put("key", true);
    		jsonObject.put("value", json);
		} catch (Exception e) {
			jsonObject.put("value", "上传失败");
			e.getMessage();
		} finally {
			ossClient.shutdown();
		}
		return jsonObject;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadPath 上传路径
	 * @param uploadFiles 需要上传的图片集合
	 * @return 是否成功+文件信息集合
	 * @description 上传多个图片
	 */
	public JSONObject uploadImage(String uploadPath, MultipartFile[] uploadFiles) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", false);
		for (MultipartFile uploadFile : uploadFiles) {
		    if (!validateImageSize(uploadFile)) {
		    	jsonObject.put("value", "上传图片不能大于"+Long.parseLong(imagesize)/1024/1024+"MB");
		    	return jsonObject;
			}
		    if (!validateImageType(uploadFile)) {
		    	jsonObject.put("value", "上传图片格式错误");
		    	return jsonObject;
			}
		}
	    OSS ossClient = null;
	    try {
	    	ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	    	List<JSONObject> list = new ArrayList<JSONObject>();
	    	JSONObject json = new JSONObject();
	    	for (MultipartFile uploadFile : uploadFiles) {
	    		String ext = getFileExt(uploadFile);
	    		String path = uploadPath+"/"+DateUtil.getYmd();
				String filename = uploadFile.getOriginalFilename();
				String realPath = path+IDUtils.getUUID()+"."+ext;
				ObjectMetadata obj = new ObjectMetadata();
				obj.setContentDisposition("attachment;filename=\"" + filename + "\"");
		    	ossClient.putObject(bucket, realPath, uploadFile.getInputStream(), obj);
	    		json = new JSONObject();
	    		json.put("filename", filename);
	    		json.put("url", url);
	    		json.put("path", realPath);
	    		list.add(json);
			}
			jsonObject.put("key", true);
			jsonObject.put("value", list);
		} catch (Exception e) {
			jsonObject.put("value", "上传失败");
			e.getMessage();
		} finally {
			ossClient.shutdown();
		}
		return jsonObject;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param path 需要删除的文件路径
	 * @return 是否成功
	 * @description 删除单个文件
	 */
	public boolean delFile(String path) {
		boolean res = false;
		OSS ossClient = null;
	    try {
	    	ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	    	ossClient.deleteObject(bucket, path);
	    	res = true;
		} catch (Exception e) {
			e.getMessage();
		} finally {
			ossClient.shutdown();
		}
	    return res;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param paths 文件路径集合
	 * @return 是否成功
	 * @description 删除多个文件
	 */
	public boolean delFiles(String[] paths) {
		boolean res = false;
		OSS ossClient = null;
	    try {
	    	ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	    	List<String> keys = new ArrayList<String>();
	    	for (String path : paths) {
	    		keys.add(path);
			}
	    	DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys));
	    	deleteObjectsResult.getDeletedObjects();
	    	res = true;
		} catch (Exception e) {
			e.getMessage();
		} finally {
			ossClient.shutdown();
		}
	    return res;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月24日
	 * @param path 文件路径
	 * @param outputStream 输出流
	 * @throws IOException 
	 * @description 下载文件
	 */
	public void download(String path, OutputStream outputStream) throws IOException {
		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		OSSObject object = ossClient.getObject(new GetObjectRequest(bucket, path));
		InputStream inputStream = object.getObjectContent();
		byte[] buffer = new byte[1024];
		int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
		// 关闭OSSClient。
		ossClient.shutdown();
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadFile 待上传的文件
	 * @return String 后缀
	 * @description 得到上传文件的后缀
	 */
	public String getFileExt(MultipartFile uploadFile) {
		return uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadImage
	 * @return 是否允许
	 * @description 判断图片大小
	 */
	public boolean validateImageSize(MultipartFile uploadImage) {
		if (uploadImage != null) {
			return uploadImage.getSize() <= Long.parseLong(imagesize);
		}
		return false;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadFile
	 * @return 是否允许
	 * @description 判断文件大小
	 */
	public boolean validateFileSize(MultipartFile uploadFile) {
		if (uploadFile != null) {
			return uploadFile.getSize() <= Long.parseLong(filesize);
		}
		return false;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadImage
	 * @return 是否合理
	 * @description 判断图片后缀类型
	 */
	public boolean validateImageType(MultipartFile uploadImage) {
		if (uploadImage != null && uploadImage.getSize() > 0L) {
			List<String> arrowExtension = Arrays.asList(new String[] {"gif", "jpg", "png", "bmp", "jpeg"});
			String ext = getFileExt(uploadImage);
			return arrowExtension.contains(ext);
		}
		return false;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param uploadFile
	 * @return 是否合理
	 * @description 判断文件后缀类型
	 */
	public boolean validateFileType(MultipartFile uploadFile) {
		if (uploadFile != null && uploadFile.getSize() > 0L) {
			List<String> arrowExtension = Arrays.asList(new String[] {"gif", "jpg", "png", "bmp", "jpeg", "doc", "docx", "ppt", "xls", "xlsx", "zip", "rar", "pdf", "txt"});
			//List<String> arrowExtension = Arrays.asList(new String[] {"doc", "docx", "pdf", "jpg"});
			String ext = getFileExt(uploadFile);
			return arrowExtension.contains(ext);
		}
		return false;
	}
}
