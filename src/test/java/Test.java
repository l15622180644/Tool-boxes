import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzk.toolboxes.ToolBoxesApplication;
import com.lzk.toolboxes.config.base.BaseParam;
import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.utils.EncodeUtil;
import com.lzk.toolboxes.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @module
 * @date 2021/6/19 14:28
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ToolBoxesApplication.class)
public class Test {

    @org.junit.Test
    public void show() throws Exception {
        System.out.println(Integer.parseInt("001"));
    }

    @org.junit.Test
    public void show1(){
        System.out.println(Arrays.toString("abc123".toCharArray()));
    }
}
