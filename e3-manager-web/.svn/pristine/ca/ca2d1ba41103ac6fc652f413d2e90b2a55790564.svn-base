package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.util.FastDFSClient;
import cn.e3mall.common.util.JsonUtils;

@Controller
public class PictureController {
	
	//获取配置文件中的属性
	@Value("${IMAGES_SERVER_URL}")
	private String IMAGES_SERVER_URL;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	//指定响应结果的content-type
	@RequestMapping("/pic/upload")
	//直接响应浏览器
	@ResponseBody
	//uploadFile与commons.js中一致
	public String uploadFile(MultipartFile uploadFile) {
		
		try {
			//创建一个FastDFS对象
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//获取图片扩展名（不包括“.”）
			String filename = uploadFile.getOriginalFilename();
			String exename = filename.substring(filename.lastIndexOf(".")+1);
			//执行上传处理，得到path路径
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), exename);
			//补充完整的URl
			String url = IMAGES_SERVER_URL + path;
			//将上传成功的数据返回（json数据）
			Map map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			String json = JsonUtils.objectToJson(map);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			//上传失败
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("massage", "上传图片失败");
			String json = JsonUtils.objectToJson(map);
			return json;
		}
		
	}
}
