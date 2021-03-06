package com.eden.fans.bs.web.controller;

import com.eden.fans.bs.common.util.GsonUtil;
import com.eden.fans.bs.domain.enu.*;
import com.eden.fans.bs.domain.mvo.PostInfo;
import com.eden.fans.bs.domain.response.BaseCodeEnum;
import com.eden.fans.bs.domain.response.PostErrorCodeEnum;
import com.eden.fans.bs.domain.response.ServiceResponse;
import com.eden.fans.bs.service.IPostService;
import com.eden.fans.bs.service.IUserPostService;
import com.eden.fans.bs.service.IUserService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 *帖子的审批（依赖管理员拦截器）
 * Created by shengyanpeng on 2016/3/25.
 */
@Controller
@RequestMapping(value = "/mpost")
public class PostManageController {
    private static Logger logger = LoggerFactory.getLogger(PostManageController.class);
    @Autowired
    private IPostService postService;
    private static Gson gson = GsonUtil.getGson();

    /**
     * 获取已审批的帖子
     * @param appCode
     * @postStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approvedPost", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String approvedPost(@RequestParam(value="appCode",required=true) String appCode,
                               @RequestParam(value="userCode",required=true) Long userCode,Integer pageNum) throws Exception {
        StringBuilder response = new StringBuilder();
        String result = postService.queryApprovedPost(appCode,userCode,pageNum);
        createResponseHeader(response,PostErrorCodeEnum.GET_APPROVED_POST_SUCCESS);
        response.append("\"result\":" + result +"}");
        return response.toString();
    }

    /**
     * 获取待审批的帖子
     * @param appCode
     * @postStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approvalPost", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String approvalPost(@RequestParam(value="appCode",required=true) String appCode,Integer pageNum) throws Exception {
        StringBuilder response = new StringBuilder();
        String result = postService.queryApprovalPost(appCode, pageNum);
        createResponseHeader(response,PostErrorCodeEnum.SUCCESS);
        response.append("\"result\":" + result +"}");
        return response.toString();
    }

    /**
     *  更新帖子状态：审批帖子
     * @param appCode
     * @param postId
     * @postStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatePost", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updatePost(@RequestParam(value="appCode",required=true) String appCode,
                             @RequestParam(value="postId",required=true) String postId,
                             Long userCode, ApprovePost approvePost) throws Exception {
            boolean result = postService.updateStatus(appCode, postId, PostStatus.getPostStatus(approvePost.getValue()), userCode);
            ServiceResponse<String> response = new ServiceResponse<String>(PostErrorCodeEnum.UPDATE_POST_SUCCESS);
            response.setResult(result+"");
            return gson.toJson(response);
    }


    /**
     *  更新帖子状态：帖子加精、取消加精
     * @param appCode
     * @param postId
     * @postStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/postOnTop", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String onTop(@RequestParam(value="appCode",required=true) String appCode,
                        @RequestParam(value="postId",required=true) String postId,
                        @RequestParam(value="onTop",required=true) PostOnTop onTop,
                        Long userCode) throws Exception {
        boolean result = postService.updateOnTop(appCode, postId, onTop, userCode);
        ServiceResponse<String> response = new ServiceResponse<String>(PostErrorCodeEnum.ONTOP_POST_SUCCESS);
        response.setResult(result+"");
        return gson.toJson(response);
    }

    /**
     *  更新帖子状态：审批帖子
     * @param appCode
     * @param postId
     * @postStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/postBoutique", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String boutique(@RequestParam(value="appCode",required=true) String appCode,
                             @RequestParam(value="postId",required=true) String postId,
                             @RequestParam(value="boutique",required=true) PostBoutique boutique,
                             Long userCode) throws Exception {
        boolean result = postService.updateBoutique(appCode,postId,boutique,userCode);
        ServiceResponse<String> response = new ServiceResponse<String>(PostErrorCodeEnum.BOUTIQUE_POST_SUCCESS);
        response.setResult(result+"");
        return gson.toJson(response);
    }

    /**
     * 创建广告帖子(只允许管理员创建)
     * @param appCode
     * @param postInfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/advertPost", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String advertPost(@RequestParam(value="appCode",required=true) String appCode,PostInfo postInfo) throws Exception {
        postInfo.setStatus(PostStatus.NORMAL);
        ServiceResponse<Boolean> response = null;
        boolean result = postService.createPost(appCode, postInfo);
        if(result){
            response = new ServiceResponse<Boolean>(PostErrorCodeEnum.CREATE_POST_SUCCESS);
        } else {
            response = new ServiceResponse<Boolean>(PostErrorCodeEnum.CREATE_POST_FAILED);
        }
        response.setResult(result);
        return gson.toJson(response);
    }

    /**
     * 获取帖子类型列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/postType", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String postType() throws Exception {
        PostType [] types = PostType.values();
        Map<String,String> response = new HashMap<String, String>();
        for(PostType type:types){
            response.put(type.name(),type.getName());
        }
        return gson.toJson(response);
    }

    private void createResponseHeader(StringBuilder response, BaseCodeEnum baseCodeEnum){
        response.append("{\"code\":" + baseCodeEnum.getCode()+",");
        response.append("\"msg\":\"" + baseCodeEnum.getMsg()+"\",");
        response.append("\"detail\":\"" + baseCodeEnum.getDetail()+"\",");
    }

}
