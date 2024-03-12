package com.my.column.controller.rest;

import com.my.column.service.ColumnService;
import com.my.column.util.Result;
import com.my.column.util.ResultGenerator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.Arrays;

@Controller
public class IndexController {

    @Resource
    private ColumnService columnService;

//    微信服务器验证
//    private static final String TOKEN = "shishi"; // Replace with your token
//    @GetMapping("/")
//    @ResponseBody
//    public String checkSignature(@RequestParam("signature") String signature,
//                                  @RequestParam("timestamp") String timestamp,
//                                  @RequestParam("nonce") String nonce,
//                                    @RequestParam("echostr") String echostr) {
//
//        String[] tmpArr = {TOKEN, timestamp, nonce};
//        Arrays.sort(tmpArr);
//        String tmpStr = String.join("", tmpArr);
//        tmpStr = getSha1(tmpStr);
//        Boolean re=tmpStr.equals(signature);
//        System.out.println(re);
//        if(re){
//            return echostr;
//        }else{
//            return "";
//        }
//
//    }
//
//    public static String getSha1(String str){
//        if(str==null||str.length()==0){
//            return null;
//        }
//        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
//                'a','b','c','d','e','f'};
//        try {
//            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
//            mdTemp.update(str.getBytes("UTF-8"));
//
//            byte[] md = mdTemp.digest();
//            int j = md.length;
//            char buf[] = new char[j*2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                buf[k++] = hexDigits[byte0 & 0xf];
//            }
//            return new String(buf);
//        } catch (Exception e) {
//            // TODO: handle exception
//            return null;
//        }
//    }
    @GetMapping({"", "/", "/index", "/index.html"})
    public String indexPage() {
        System.out.println("index");
        return "index";
    }

    @GetMapping("/randomColumns")
    @ResponseBody
    public Result randomColumns() {
        Result result = ResultGenerator.genSuccessResult();
        result.setData(columnService.getRandomColumnsForIndex());
        return result;
    }
}
