package com.syc.china.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syc.china.mapper.*;
import com.syc.china.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class CarSourceService {

    @Autowired
    private CarSourceMapper carSourceMapper;
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
    private DistrictMapper districtMapper;


/*    *//**
     * 查询车源集合，并将车源对象的信息补全
     * @return
     */
    public List<CarSource> queryAll(Integer pageNum,Integer pageSize,String key) {

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
        Example example = new Example(CarSource.class);

        if(org.apache.commons.lang.StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("carName","%" + key + "%");
        }

        List<CarSource> cars = carSourceMapper.selectByExample(example);

        for (CarSource car : cars) {
            String pName = provinceMapper.selectByPrimaryKey(car.getProvinceId()).getName();
            String cName = cityMapper.selectByPrimaryKey(car.getCityId()).getName();
            String dName = districtMapper.selectByPrimaryKey(car.getDistrictId()).getName();

            String pEndName = provinceMapper.selectByPrimaryKey(car.getProvinceIdEnd()).getName();
            String cEndName = cityMapper.selectByPrimaryKey(car.getCityIdEnd()).getName();
            String dEndName = districtMapper.selectByPrimaryKey(car.getDistrictIdEnd()).getName();
            car.setStartName(pName+cName+dName);
            car.setEndName(pEndName+cEndName+dEndName);

            car.setCarName(carMapper.selectByPrimaryKey(car.getCarId()).getName());

            car.setGoodsName(goodsMapper.selectByPrimaryKey(car.getGoodsId()).getName());


            car.setTransportName(transportMapper.selectByPrimaryKey(car.getTransportId()).getName());
            car.setUnitName(unitMapper.selectByPrimaryKey(car.getUnitId()).getName());
        }

        return cars;
    }
   /* public PageResult<CarSource> queryCarsourcesPage(Integer page, Integer rows,String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(CarSource.class);
        if (StringUtils.isNotBlank(key)) {
            //此处要注意空格 代码紧密会报错sql语法错误
            example.createCriteria().andLike("ownerName", "%" + key + "%");
        }
        List<CarSource> CarSource = carSourceMapper.selectByExample(example);
        PageInfo<CarSource> info = new PageInfo<>(CarSource);
        PageResult<CarSource> page1 = new PageResult<>();
        page1.setTotal(info.getTotal());
        page1.setItems(CarSource);
        page1.setTotalPage(Long.valueOf(info.getPages()));
        return page1;
    }*/
    /**
     * 添加货源的方法
     * @param carSource
     * @return
     */
    public int insert(CarSource carSource) {
        carSource.setStartTime(new Date());
        return carSourceMapper.insert(carSource);
    }

    /**
     * 删除货源的方法
     * @param id
     */
    public void deleteById(Integer id) {
        carSourceMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询车辆类型集合
     * @return
     */
    public List<Car> getCars() {
        return carMapper.selectAll();
    }

    /**
     * 查询单位集合
     * @return
     */
    public List<Unit> getUnits() {
        return unitMapper.selectAll();
    }

    /**
     * 查询可载种类集合
     * @return
     */
    public List<Goods> getGoods() {
        return goodsMapper.selectAll();
    }

    /**
     * 查询运输类型集合
     * @return
     */
    public List<Transport> getTransports() {
        return transportMapper.selectAll();
    }

    /**
     * 获得车源对象，并将信息补全
     * @param id
     * @return
     */
    public CarSource queryCarSource(Integer id) {
        CarSource carSource = carSourceMapper.selectByPrimaryKey(id);
        String pName = provinceMapper.selectByPrimaryKey(carSource.getProvinceId()).getName();
        String cName = cityMapper.selectByPrimaryKey(carSource.getCityId()).getName();
        String dName = districtMapper.selectByPrimaryKey(carSource.getDistrictId()).getName();

        String pEndName = provinceMapper.selectByPrimaryKey(carSource.getProvinceIdEnd()).getName();
        String cEndName = cityMapper.selectByPrimaryKey(carSource.getCityIdEnd()).getName();
        String dEndName = districtMapper.selectByPrimaryKey(carSource.getDistrictIdEnd()).getName();
        carSource.setStartName(pName+cName+dName);
        carSource.setEndName(pEndName+cEndName+dEndName);

        carSource.setCarName(carMapper.selectByPrimaryKey(carSource.getCarId()).getName());

        carSource.setGoodsName(goodsMapper.selectByPrimaryKey(carSource.getGoodsId()).getName());


        carSource.setTransportName(transportMapper.selectByPrimaryKey(carSource.getTransportId()).getName());
        carSource.setUnitName(unitMapper.selectByPrimaryKey(carSource.getUnitId()).getName());
        return carSource;
    }

    /**
     * 查询省 集合
     * @return
     */
    public List<Province> queryProvinces() {
        return provinceMapper.selectAll();
    }


    public Province queryCurrPro(Integer id){
        return provinceMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询市 集合
     * @return
     */
    public List<City> queryCitys() {
        return cityMapper.selectAll();

    }


    public City queryCurrCity(Integer cityId) {
        return cityMapper.selectByPrimaryKey(cityId);
    }


    /**
     * 查询 区 集合
     * @return
     */
    public List<District> queryDistricts() {
        return districtMapper.selectAll();

    }

    public District queryCurrDis(Integer districtId) {
        return districtMapper.selectByPrimaryKey(districtId);
    }

    /**
     * 查询车辆类型
     * @param carId
     * @return
     */
    public Car queryCar(Integer carId) {
        return carMapper.selectByPrimaryKey(carId);
    }

    /**
     * 查询单位类型
     * @param unitId
     * @return
     */
    public Unit queryUnit(Integer unitId) {
        return unitMapper.selectByPrimaryKey(unitId);
    }

    /**
     * 查询可载货物类型
     * @param id
     * @return
     */
    public Goods queryGood(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }


    public Transport querycurrTransport(Integer transportId) {
        return transportMapper.selectByPrimaryKey(transportId);
    }

    /**
     * 更新车源
     * @param carSource
     * @return
     */
    public int update(CarSource carSource) {
        return carSourceMapper.updateByPrimaryKeySelective(carSource);
    }
}
