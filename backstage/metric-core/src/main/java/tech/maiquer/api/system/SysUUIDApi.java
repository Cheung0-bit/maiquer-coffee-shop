package tech.maiquer.api.system;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.maiquer.common.utils.UUIDUtils;

@RequestMapping("/system/uuid")
@CrossOrigin
@RestController
@Api(tags = {"UUID工具"})
public class SysUUIDApi {

    @GetMapping("/getName")
    public String getUuidName() {
        return UUIDUtils.createName();
    }

}
