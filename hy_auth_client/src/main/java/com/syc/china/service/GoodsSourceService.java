package com.syc.china.service;


import com.github.pagehelper.PageHelper;
import com.syc.china.mapper.*;
import com.syc.china.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class GoodsSourceService {

    @Autowired
    private GoodsSourceMapper goodsSourceMapper;

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private UnitMapper unitMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private TransportMapper transportMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;
    @Autowired
    private DistrictMapper districtMapper;


    /**
     * 查询货源集合，并将信息补全
     * @return
     */
    public List<GoodsSource> queryAll(Integer pageNum,Integer pageSize,String key){
        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        PageHelper.startPage(pageNum,pageSize);
        //过滤
        Example example = new Example(GoodsSource.class);

        if(org.apache.commons.lang.StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("goodsName","%" + key + "%");
        }

        List<GoodsSource> goods = goodsSourceMapper.selectByExample(example);

        for (GoodsSource good : goods ) {

            String pName = provinceMapper.selectByPrimaryKey(good.getProvinceId()).getName();
            String cName = cityMapper.selectByPrimaryKey(good.getCityId()).getName();
            String dName = districtMapper.selectByPrimaryKey(good.getDistrictId()).getName();

            String pEndName = provinceMapper.selectByPrimaryKey(good.getProvinceIdEnd()).getName();
            String cEndName = cityMapper.selectByPrimaryKey(good.getCityIdEnd()).getName();
            String dEndName = districtMapper.selectByPrimaryKey(good.getDistrictIdEnd()).getName();
            good.setStartName(pName+cName+dName);
            good.setEndName(pEndName+cEndName+dEndName);


            good.setStartTime(new Date());
            good.setCarName(carMapper.selectByPrimaryKey(good.getCarId()).getName());
            good.setGoodsIdName(goodsMapper.selectByPrimaryKey(good.getGoodsId()).getName());
            good.setGoodsTypeName(goodsTypeMapper.selectByPrimaryKey(good.getGoodsTypeId()).getName());
            good.setUnitName(unitMapper.selectByPrimaryKey(good.getUnitId()).getName());
            good.setTransportName(transportMapper.selectByPrimaryKey(good.getTransportId()).getName());
        }

        return goods;
    }


    /**
     * 添加货源方法
     * @param goodsSource
     * @return
     */
    public int insert(GoodsSource goodsSource) {
        return goodsSourceMapper.insertSelective(goodsSource);
    }

    /**
     * 删除货源方法
     * @param id
     */
    public void deleteById(Integer id) {
        goodsSourceMapper.deleteByPrimaryKey(id);
    }


    /**
     *查询所有车辆类型
     * @return
     */
    public List<Car> getCars() {
        return carMapper.selectAll();
    }


    /**
     *查询所有单位类型
     * @return
     */
    public List<Unit> getUnits() {
        return unitMapper.selectAll();
    }

    /**
     *查询所有货物类型
     * @return
     */
    public List<Goods> getGoods() {
        return goodsMapper.selectAll();
    }

    /**
     *查询所有运输类型
     * @return
     */
    public List<Transport> getTransports() {
        return transportMapper.selectAll();
    }

    /**
     * 传销货源对象并将信息补全
     * @param id
     * @return
     */
    public GoodsSource queryGoodsSource(Integer id) {
        GoodsSource goodsSource = goodsSourceMapper.selectByPrimaryKey(id);
        String pName = provinceMapper.selectByPrimaryKey(goodsSource.getProvinceId()).getName();
        String cName = cityMapper.selectByPrimaryKey(goodsSource.getCityId()).getName();
        String dName = districtMapper.selectByPrimaryKey(goodsSource.getDistrictId()).getName();

        String pEndName = provinceMapper.selectByPrimaryKey(goodsSource.getProvinceIdEnd()).getName();
        String cEndName = cityMapper.selectByPrimaryKey(goodsSource.getCityIdEnd()).getName();
        String dEndName = districtMapper.selectByPrimaryKey(goodsSource.getDistrictIdEnd()).getName();
        goodsSource.setStartName(pName+cName+dName);
        goodsSource.setEndName(pEndName+cEndName+dEndName);

        goodsSource.setCarName(carMapper.selectByPrimaryKey(goodsSource.getCarId()).getName());

        goodsSource.setGoodsIdName(goodsMapper.selectByPrimaryKey(goodsSource.getGoodsId()).getName());

        goodsSource.setGoodsTypeName(goodsTypeMapper.selectByPrimaryKey(goodsSource.getGoodsTypeId()).getName());

        goodsSource.setTransportName(transportMapper.selectByPrimaryKey(goodsSource.getTransportId()).getName());
        goodsSource.setUnitName(unitMapper.selectByPrimaryKey(goodsSource.getUnitId()).getName());

        return goodsSource;
    }

    /**
     * 查询货物种类
     * @return
     */
    public List<GoodsType> getGoodsType() {
        return goodsTypeMapper.selectAll();
    }

    /**
     * 更新货源
     * @param goodsSource
     * @return
     */
    public int update(GoodsSource goodsSource) {
        return goodsSourceMapper.updateByPrimaryKeySelective(goodsSource);
    }
}
