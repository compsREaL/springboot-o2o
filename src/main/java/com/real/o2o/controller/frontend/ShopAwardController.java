package com.real.o2o.controller.frontend;

import com.real.o2o.dto.AwardExecution;
import com.real.o2o.entity.Award;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.UserShopMap;
import com.real.o2o.service.AwardService;
import com.real.o2o.service.UserShopMapService;
import com.real.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/8 14:35
 */
@Controller
@RequestMapping("/frontend")
public class ShopAwardController {

    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 根据店铺id获取该店铺下的所有奖品列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listawardsbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listAwardsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //获取店铺id
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        //空值校验
        if (pageIndex>-1 && pageSize>-1 && shopId>-1 ){
            //获取前端可能传入的奖品名模糊查询
            String awardName = HttpServletRequestUtil.getString(request,"awardName");
            //拼接查询条件
            Award awardCondition = compactAwardCondition4Search(shopId,awardName);
            AwardExecution awardExecution = awardService.getAwardList(awardCondition,pageIndex,pageSize);
            modelMap.put("awardList",awardExecution.getAwardList());
            modelMap.put("count",awardExecution.getCount());
            modelMap.put("success",true);
            //从session中获取用户信息，主要为了显示用户在该店铺的积分
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user!=null && user.getUserId()!=null){
                //获取用户在本店铺的积分
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(),shopId);
                if (userShopMap!=null){
                    modelMap.put("totalPoint",userShopMap.getPoint());
                } else {
                    modelMap.put("totalPoint",0);
                }
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    /**
     * 封装查询条件
     * @param shopId
     * @param awardName
     * @return
     */
    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award award = new Award();
        award.setShopId(shopId);
        //取出可用奖品
        award.setEnableStatus(1);
        if (awardName!=null){
            award.setAwardName(awardName);
        }
        return award;
    }
}
