package org.seckillproject.service.impl;


import org.seckillproject.dao.ItemDOMapper;
import org.seckillproject.dao.ItemStockDOMapper;
import org.seckillproject.dataobject.ItemDO;
import org.seckillproject.dataobject.ItemStockDO;
import org.seckillproject.error.BusinessException;
import org.seckillproject.error.EmBusinessError;
import org.seckillproject.service.ItemService;
import org.seckillproject.service.model.ItemModel;
import org.seckillproject.validator.ValidationResult;

import org.seckillproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author:ZhangYu
 * @date:2021/09/16 20:24
 */
@Service
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ValidatorImpl validator;
    
    @Autowired
    private ItemDOMapper itemDOMapper;
    
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    
    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        
        return itemDO;
    }
    
    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel){
        
        if (itemModel == null){
            return null;
        }
        
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        
        
        return itemStockDO;
    }
    
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        ValidationResult result  = validator.validate(itemModel);
        //校验入参
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        
        //转化itemmodel -> dataobject
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);
        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        
        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        return null;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null){
            return null;
        }
        
        //操作获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        
        //将dataobject -> model
        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);
        
        return itemModel;
    }
    
    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        
        ItemModel itemModel = new ItemModel();
        
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        
        return itemModel;
    }
    
}
