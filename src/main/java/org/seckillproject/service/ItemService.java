package org.seckillproject.service;

import org.seckillproject.error.BusinessException;
import org.seckillproject.service.model.ItemModel;

import java.util.List;

/**
 * @author:ZhangYu
 * @date:2021/09/16 20:10
 */
public interface ItemService {
    
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //删除商品
    
    //修改商品
    
    //商品列表浏览
    List<ItemModel> listItem();
    
    //商品详情浏览
    ItemModel getItemById(Integer id);
}
