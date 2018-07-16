package com.taikang.wechat.service.impl.authorizedimpl;

import com.alibaba.fastjson.JSON;
import com.taikang.wechat.config.handleexception.ControllerException;
import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.dao.authorDao.AuthorDao;
import com.taikang.wechat.model.weChat.*;
import com.taikang.wechat.service.authorized.AuthorizedService;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import com.taikang.wechat.service.precode.PreCodeService;
import com.taikang.wechat.utils.WeChatUtils;
import com.taikang.wechat.utils.aes.AesException;
import com.taikang.wechat.utils.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * 微信授权
 *
 * @author 张清森
 * 2018.7.9
 */
@Service
@Slf4j
public class AuthorizedServiceImpl implements AuthorizedService {
    private final VerifyTicketService ticketService;
    private final ComponentAcceptTokenService acceptTokenService;
    private final PreCodeService preCodeService;
    private final AuthorDao authorDao;

    @Autowired
    public AuthorizedServiceImpl(VerifyTicketService ticketService,
                                 ComponentAcceptTokenService acceptTokenService,
                                 PreCodeService preCodeService, AuthorDao authorDao) {
        this.ticketService = ticketService;
        this.acceptTokenService = acceptTokenService;
        this.preCodeService = preCodeService;

        this.authorDao = authorDao;
    }

    /**
     * 处理微信授权事件
     *
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void handleAuthorize(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        String timestamp = request.getParameter("timestamp");
        String encryptType = request.getParameter("encrypt_type");
        String nonce = request.getParameter("nonce");
        String msgSignature = request.getParameter("msg_signature");
        log.info("timestamp:" + timestamp);
        log.info("encrypt_type:" + encryptType);
        log.info("nonce:" + nonce);
        log.info("msg_signature:" + msgSignature);
        boolean checkSignature = WeChatUtils.checkSignature(WeChatContants.TOKEN, msgSignature, timestamp, nonce);
        if (!checkSignature) {
            log.info("签名错误");
            return;
        }
        //======================获取getComponentVerifyTicket===============//
        String componentVerifyTicket = getComponentVerifyTicket(request, timestamp, nonce, msgSignature);
        VerifyTicket verifyTicket = new VerifyTicket();
        verifyTicket.setComponentVerifyTicket(componentVerifyTicket);
        //保存凭证
        ticketService.insertTicket(verifyTicket);
        //=====================调用接口获取component_access_token=============//
        String componentAccessToken;
        //判断是否调用微信接口
        if (isGetAcceptToken()){
            WeChatComponentAccessTokenVo componentAccessTokenVo =WeChatUtils.getComponentAccessToken(componentVerifyTicket);
            componentAccessToken=componentAccessTokenVo.getComponent_access_token();
            ComponentAcceptToken componentAcceptToken = new ComponentAcceptToken();
            componentAcceptToken.setAcceptToken(componentAccessTokenVo.getComponent_access_token());
            componentAcceptToken.setExpiresIn(componentAccessTokenVo.getExpires_in());
            //保存令牌
            acceptTokenService.insertAcceptToken(componentAcceptToken);
        }else {
            ComponentAcceptToken componentAcceptTokenM = acceptTokenService.selectAcceptToken();
            componentAccessToken=componentAcceptTokenM.getAcceptToken();
        }
        //===================获取预授权码pre_auth_code*************************//
        Object[] object = {componentAccessToken};
        //调用微信
        WeChatPreAuthCodeVo preAuthCodeVo = WeChatUtils.getWeChatPreAuthCodeVo(object);
        PreCode preCode = new PreCode();
        preCode.setPreCode(preAuthCodeVo.getPre_auth_code());
        preCode.setExpiresIn(preAuthCodeVo.getExpires_in());
        //将预授权码存在数据库中
        preCodeService.updatePreCodeByPreCodeId(preCode);
    }
    /**
     * 新增授权信息
     * @param bigAuthorizationInfo 授权信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertAuthorService(BigAuthorizationInfo bigAuthorizationInfo) {
        if (bigAuthorizationInfo==null){
            throw  new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        authorDao.insertAuthorDao(bigAuthorizationInfo);
    }
    /**
     * 刷新令牌
     * @return 授权信息
     */
    @Override
    public AuthorizationInfo doRefreshToken(String authorizationInfoId) {
        //第三方id
        String thridAppid = WeChatContants.THRID_APPID;
        //查询授权信息 私有内部使用  todo 编写公共查看接口 附带被迫刷新令牌
        BigAuthorizationInfo bigAuthorizationInfo = this.getAuthorInfoService(authorizationInfoId);
        if (bigAuthorizationInfo==null){
            throw  new ServiceException(HRSCExceptionEnum.UNABLE_GET_AUTHOR_INFO_BY_ID);
        }
        if (StringUtils.isEmpty(thridAppid)){
            throw  new ServiceException(HRSCExceptionEnum.CONFIG_INFO_ERROR);
        }
        ComponentAcceptToken componentAcceptToken = acceptTokenService.selectAcceptToken();
        if (componentAcceptToken==null){
            //todo 调用被迫刷新机制
            throw  new ServiceException(HRSCExceptionEnum.COMPONENT_ACCESS_TOKEN_MISS);
        }
        String url = WeChatContants.REFRESH_AUTHORIZER_ACCESS_TOKEN;
        String formatUrl = String.format(url, componentAcceptToken.getAcceptToken());
        GetRefreshTokenVo getRefreshTokenVo = new GetRefreshTokenVo();
        getRefreshTokenVo.setAuthorizer_appid(bigAuthorizationInfo.getAuthorizer_appid());
        getRefreshTokenVo.setAuthorizer_refresh_token(bigAuthorizationInfo.getAuthorizer_refresh_token());
        getRefreshTokenVo.setComponent_appid(thridAppid);
        String resultJson = WeChatUtils.postUrl(formatUrl, getRefreshTokenVo);
        AuthorizationInfo authorizationInfo = JSON.parseObject(resultJson, AuthorizationInfo.class);
        authorizationInfo.setAuthorizer_appid(authorizationInfoId);
        return authorizationInfo;
    }
    /**
     * 通过id查询授权信息
     * @param authorizationInfoId 授权信息主键id
     * @return 全部授权信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {ServiceException.class,Exception.class})
    public BigAuthorizationInfo findBigAuthorizationInfoByid(String authorizationInfoId) {
        if (StringUtils.isEmpty(authorizationInfoId)){
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        BigAuthorizationInfo authorInfoService = this.getAuthorInfoService(authorizationInfoId);
        if (authorInfoService==null){
            //todo 考虑是否需要跳转授权页面
            throw new ServiceException(HRSCExceptionEnum.UNABLE_GET_AUTHOR_INFO_BY_ID);
        }
        BigAuthorizationInfo bigAuthorizationInfo= authorDao.findBigAuthorizationInfoByIdDao(authorizationInfoId);
        //被动刷新
        if (getaBoolean(bigAuthorizationInfo)){
            //刷新token
            AuthorizationInfo authorizationInfo = this.doRefreshToken(authorizationInfoId);
            //更新数据库
            bigAuthorizationInfo.setBegTime(System.currentTimeMillis()/1000L);
            bigAuthorizationInfo.setExpires_in(authorizationInfo.getExpires_in());
            bigAuthorizationInfo.setAuthorizer_access_token(authorizationInfo.getAuthorizer_access_token());
            bigAuthorizationInfo.setAuthorizer_refresh_token(authorizationInfo.getAuthorizer_refresh_token());
            this.updateAuthorizationInfoById(bigAuthorizationInfo);
        }
        return bigAuthorizationInfo;
    }

    /**
     * 校验是否超时
     * @param bigAuthorizationInfo 授权信息
     * @return 是否
     */
    private Boolean getaBoolean(BigAuthorizationInfo bigAuthorizationInfo) {
        Long begTime = bigAuthorizationInfo.getBegTime();
        Long currentTimeMillis = System.currentTimeMillis();
        Long tokenExperiIn = WeChatContants.TOKEN_EXPERI_IN;
        Long middle=(currentTimeMillis-begTime)/1000L;
        Long expiresIn = bigAuthorizationInfo.getExpires_in();
        Long chazhi = middle-expiresIn;
        return chazhi<=tokenExperiIn;
    }

    /**
     * 更新授权信息
     * @param bigAuthorizationInfo 授权信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED ,rollbackFor = {ServiceException.class,Exception.class})
    public void updateAuthorizationInfoById(BigAuthorizationInfo bigAuthorizationInfo) {
        if (bigAuthorizationInfo==null){
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        authorDao.updateAuthorizationInfoByIdDao(bigAuthorizationInfo);
    }
    /**
     * 变更粉丝数量
     * @param toUserName appid
     * @param i 变更类型
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED ,rollbackFor = {ServiceException.class,Exception.class})
    public void updateAuthorizationInfoFensiNumByAppid(String toUserName, int i) {
        BigAuthorizationInfo bigAuthorizationInfo = this.getAuthorInfoByAppidService(toUserName);
        if (bigAuthorizationInfo==null){
            throw  new ServiceException(HRSCExceptionEnum.UNABLE_GET_AUTHOR_INFO_BY_ID);
        }
        Long total=bigAuthorizationInfo.getFensiNum()==null?0:bigAuthorizationInfo.getFensiNum();
        if (i==0){
            if (total.compareTo(new Long("0"))>0){
                bigAuthorizationInfo.setFensiNum(total-1);
            }
        }else if (i==1){
            bigAuthorizationInfo.setFensiNum(total+1);
        }else {

        }
        authorDao.updateAuthorizationInfoFensiNumByAppid(bigAuthorizationInfo);
    }
    /**
     * 通过appid查询授权信息
     * @param toUserName appid
     * @return 授权信息
     */
    @Override
    public BigAuthorizationInfo getAuthorInfoByAppidService(String toUserName) {
        if (StringUtils.isEmpty(toUserName)){
            return null;
        }
        return authorDao.getAuthorInfoByAppidDao(toUserName);
    }

    /**
     * 内部获取授权基本信息
     * @param authorizationInfoId 主键id
     * @return 授权信息
     */
    private BigAuthorizationInfo getAuthorInfoService(String authorizationInfoId) {
        if (StringUtils.isEmpty(authorizationInfoId)){
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        return authorDao.findAuthorizationInfoByIdDao(authorizationInfoId);
    }

    private boolean isGetAcceptToken() {
        ComponentAcceptToken componentAcceptTokenPre = acceptTokenService.selectAcceptToken();
        long t = System.currentTimeMillis()/1000L;
        if (componentAcceptTokenPre != null) {
            Long begTime = componentAcceptTokenPre.getBegTime();
            Long expiresInPre = componentAcceptTokenPre.getExpiresIn();
            Long middle = t - begTime;
            Long aLong = (middle - expiresInPre);
            if (aLong.compareTo(new Long("1200")) <= 0) {
                return true;
            }
        }else {
            return true;
        }
        return false;
    }


    /**
     * 获取凭证
     *
     * @param request      请求
     * @param timestamp    时间戳
     * @param nonce        附加信息
     * @param msgSignature 验证
     * @return String
     */

    private String getComponentVerifyTicket(HttpServletRequest request, String timestamp, String nonce, String msgSignature) throws IOException, AesException {
        String xml = getString(request);
        log.info("微信推送的原生：" + xml);
        // 第三方平台组件加密密钥
        String encodingAesKey = WeChatContants.ENCODING_AES_KEY;
        String appId = WeChatContants.THRID_APPID;
        String token = WeChatContants.TOKEN;

        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
        log.info("解密后的：" + xml);
        Map<String, String> parseXml = WeChatUtils.parseXml(xml);
        String componentVerifyTicket = parseXml.get("ComponentVerifyTicket");
        log.info(componentVerifyTicket);
        return componentVerifyTicket;
    }

    /**
     * 获取请求中的xml 变为String
     *
     * @param request 请求
     * @return String
     */
    private String getString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
