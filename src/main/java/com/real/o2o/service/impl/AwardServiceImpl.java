package com.real.o2o.service.impl;

import com.real.o2o.dao.AwardDao;
import com.real.o2o.dto.ImageHolder;
import com.real.o2o.entity.Award;
import com.real.o2o.enums.AwardStateEnum;
import com.real.o2o.dto.AwardExecution;
import com.real.o2o.exception.AwardOperationException;
import com.real.o2o.service.AwardService;
import com.real.o2o.util.ImageUtil;
import com.real.o2o.util.PageCalculator;
import com.real.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 21:23
 */
@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardDao awardDao;

    @Override
    public AwardExecution getAwardList(Award awardCondition, Integer pageIndex, Integer pageSize) {
        if (awardCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            List<Award> awardList = awardDao.queryAwardList(awardCondition,beginIndex,pageSize);
            int count = awardDao.queryAwardCount(awardCondition);
            AwardExecution awardExecution = new AwardExecution();
            awardExecution.setCount(count);
            awardExecution.setAwardList(awardList);
            return awardExecution;
        } else {
            return null;
        }

    }

    @Override
    public Award getAwardById(Long awardId) {
        return awardDao.queryAwardById(awardId);
    }

    @Override
    @Transactional
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {
        if (award != null && award.getShopId() != null) {
            award.setLastEditTime(new Date());
            award.setCreateTime(new Date());
            //奖品设置为默认可用
            award.setEnableStatus(1);
            if (thumbnail != null) {
                addThumbnail(award, thumbnail);
            }
            try {
                //添加奖品信息
                int effectRows = awardDao.insertAward(award);
                if (effectRows <= 0) {
                    throw new AwardOperationException("创建奖品失败");
                }
                return new AwardExecution(AwardStateEnum.SUCCESS, award);
            } catch (Exception e) {
                throw new AwardOperationException("创建奖品失败：" + e.getMessage());
            }
        } else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    /**
     * 若缩略图参数有值，则先处理缩略图，
     * 若原先存在缩略图中先删除原图再进行添加，再更新tb_award信息
     *
     * @param award
     * @param thumbnail
     * @return
     */
    @Override
    @Transactional
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {
        if (award!=null && award.getAwardId()!=null){
            award.setLastEditTime(new Date());
            if (thumbnail!=null){
                Award tempAward = awardDao.queryAwardById(award.getAwardId());
                if (tempAward.getAwardImg()!=null){
                    //原图片的删除
                    ImageUtil.deleteFileOrPath(tempAward.getAwardImg());
                }
                //存储图片流
                addThumbnail(award,thumbnail);
            }
            try {
                //根据传入的实体类修改响应的信息
                int effectRows = awardDao.updateAward(award);
                if (effectRows<=0){
                    throw new AwardOperationException("更新奖品信息失败");
                }else{
                    return new AwardExecution(AwardStateEnum.SUCCESS,award);
                }
            } catch (Exception e){
                throw new AwardOperationException("更新奖品信息失败："+e.getMessage());
            }
        } else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    private void addThumbnail(Award award, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(award.getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        award.setAwardImg(thumbnailAddr);
    }
}
