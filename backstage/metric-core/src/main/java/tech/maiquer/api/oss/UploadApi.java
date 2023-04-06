package tech.maiquer.api.oss;

import com.upyun.UpException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.maiquer.common.model.Result;
import tech.maiquer.upyun.utils.UpYunUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static tech.maiquer.common.model.ResultCode.IMAGE_TYPE_NOT_SUPPORT;
import static tech.maiquer.common.model.ResultCode.INSERT_FAIL;

/**
 * @author Lin
 */
@RestController
@CrossOrigin
@Slf4j
@Api(tags = "上传文件接口")
@RequestMapping("/api/upload")
public class UploadApi {

    Map<String, String> map;

    @Resource
    private UpYunUtils upYunUtils;

    public UploadApi() {
        map = new HashMap<>();
        map.put("jpg", null);
        map.put("png", null);
        map.put("jpeg", null);
        map.put("icon", null);
        map.put("gif", null);
    }

    @PostMapping("/image")
    public Result upload(@RequestParam(value = "imgFile", required = false) MultipartFile image) {

        try {
            if (image.getSize() > 4194304) {
                return Result.failure(INSERT_FAIL, "上传图片大小必须在4MB以内！");
            }
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(INSERT_FAIL, "请先上传图片！");
        }

        String[] temp = image.getOriginalFilename().split("\\.");
        String fileType = temp[temp.length - 1].toLowerCase();

        if (!map.containsKey(fileType)) {
            return Result.failure(IMAGE_TYPE_NOT_SUPPORT);
        }

        String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "." + fileType;
        boolean success = false;

        try {
            success = upYunUtils.uploadImg(fileName, image.getBytes());
        } catch (IOException | UpException e) {
            log.error(e.toString());
            return Result.failure("图片上传异常");
        }

        return success ? Result.success("https://images.maiquer.tech/images/" + fileName) : Result.failure("图片上传失败!");

    }

}
