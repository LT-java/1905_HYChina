package com.syc.china.service;

import com.syc.china.mapper.CityMapper;
import com.syc.china.mapper.DistrictMapper;
import com.syc.china.mapper.ProvinceMapper;
import com.syc.china.pojo.City;
import com.syc.china.pojo.District;
import com.syc.china.pojo.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private DistrictMapper districtMapper;



    public List<Province> getProvince(){
        List<Province> pros = provinceMapper.selectAll();
        return pros;
    }


    public List<City> getCity(Integer proId) {
        City city = new City();
        city.setProId(proId);
        return cityMapper.select(city);

    }

    public List<District> getDistrict(Integer cityId) {
        District district = new District();
        district.setCityId(cityId);
        return districtMapper.select(district);
    }
}
