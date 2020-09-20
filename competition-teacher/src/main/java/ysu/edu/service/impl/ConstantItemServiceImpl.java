package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import ysu.edu.pojo.ConstantItem;
import ysu.edu.mapper.ConstantItemMapper;
import ysu.edu.service.IConstantItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 常数项表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class ConstantItemServiceImpl extends ServiceImpl<ConstantItemMapper, ConstantItem> implements IConstantItemService {
    @Override
    public Object list(ConstantItem constantItem) {
        // 如果 有name传过来 就按照name 模糊查询
        QueryWrapper<ConstantItem> wrapper = new QueryWrapper<>();

        wrapper.orderByAsc("type_id");
        wrapper.orderByAsc("sort");

        // 如果分页返回 IPage 如果不分页 返回 List
        if(constantItem.getWithPage() == 1) {
            if(StringUtils.isNotBlank(constantItem.getName())) {
                wrapper.like("t.`name`",constantItem.getName());
            }
            return getBaseMapper().list(new Page<>(constantItem.getPageNum(),constantItem.getPageSize()),wrapper);

        } else {
            if(StringUtils.isNotBlank(constantItem.getName())) {
                wrapper.like("name",constantItem.getName());
            }
            return this.list(wrapper);
        }

    }


    @Override
    public boolean add(ConstantItem constantItem) {
        QueryWrapper<ConstantItem> wrapper = new QueryWrapper<>();
        wrapper.eq("type_id", constantItem.getTypeId());
        wrapper.eq("code",constantItem.getCode());
        ConstantItem query = this.getOne(wrapper);
        if(query == null )
            return this.save(constantItem);
        return false;
    }

    @Override
    public boolean batchdel(Integer[] ids) {
        List<Integer> list = new ArrayList<>(ids.length);
        Collections.addAll(list,ids);
        ConstantItem constantItem = new ConstantItem();
        constantItem.setActive(0);
        UpdateWrapper<ConstantItem> wrapper = new UpdateWrapper<>();
        wrapper.in("id",list);
        return this.update(constantItem,wrapper);
    }
}
