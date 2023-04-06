package tech.maiquer.upyun.utils;

import com.upyun.RestManager;
import com.upyun.UpException;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Lin
 */
@Component
public class UpYunUtils {

    private static final String BUCKET_NAME = "maiqu-index";

    private static final String OPERATOR_NAME = "maiquer";

    private static final String OPERATOR_PWD = "cAaJjKK2Sqq0ek1SbQQHHVobezWMZRsX";

    private static final String ROOT_PATH = "/images/";

    private RestManager restManager = new RestManager(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);

    public boolean uploadImg(String fileName, byte[] bytes) throws IOException, UpException {

        String lastPath = ROOT_PATH + fileName;

        Response response = restManager.writeFile(lastPath, bytes, null);
        return response.isSuccessful();

    }


}
