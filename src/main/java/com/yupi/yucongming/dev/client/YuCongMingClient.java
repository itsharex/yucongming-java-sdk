package com.yupi.yucongming.dev.client;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.yupi.yucongming.dev.utils.SignUtils.genSign;

/**
 * 调用鱼聪明 AI 的客户端
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class YuCongMingClient {

    private static final String HOST = "https://www.yucongming.com/api/dev";

    private final String accessKey;

    private final String secretKey;

    public YuCongMingClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 对话
     *
     * @param devChatRequest
     * @return
     */
    public BaseResponse<DevChatResponse> doChat(DevChatRequest devChatRequest) {
        String url = HOST + "/chat";
        String json = JSONUtil.toJsonStr(devChatRequest);
        String result = HttpRequest.post(url)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute()
                .body();
        return JSONUtil.toBean(result, BaseResponse.class);
    }

    /**
     * 获取请求头
     *
     * @param body 请求参数
     * @return
     */
    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        String encodedBody = URLEncodeUtil.encode(body, StandardCharsets.UTF_8);
        hashMap.put("body", encodedBody);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(encodedBody, secretKey));
        return hashMap;
    }

    public static void main(String[] args) {
        String accessKey = "你的 accessKey";
        String secretKey = "你的 secretKey";
        YuCongMingClient yuCongMingClient = new YuCongMingClient(accessKey, secretKey);
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1651468516836098050L);
        devChatRequest.setMessage("邓紫棋");
        BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        System.out.println(devChatResponseBaseResponse);
    }
}