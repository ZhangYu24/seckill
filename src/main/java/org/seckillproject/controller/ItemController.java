package org.seckillproject.controller;

import org.seckillproject.controller.viewobject.ItemVO;
import org.seckillproject.error.BusinessException;
import org.seckillproject.response.CommonReturnType;
import org.seckillproject.service.ItemService;
import org.seckillproject.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author:ZhangYu
 * @date:2021/09/16 23:13
 */
@Controller("item")
@RequestMapping("/item")
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
public class ItemController extends BaseController{
    
    @Autowired
    private ItemService itemService;
    
    //创建商品的controller
    @RequestMapping(value = "/createItem",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl
                                       ) throws BusinessException {
        
        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(itemModelForReturn);
        
        return CommonReturnType.create(itemVO);
        
        
        
        
    }
    
    private ItemVO convertVOFromModel(ItemModel itemModel){
        
        if (itemModel == null){
            return null;
        }
        
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        
        return itemVO;
    }
}
