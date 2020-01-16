package com.syc.china.web;


import com.github.pagehelper.PageInfo;
import com.syc.china.mapper.GoodsTypeMapper;
import com.syc.china.pojo.*;
import com.syc.china.service.CarSourceService;
import com.syc.china.service.GoodsSourceService;
import com.syc.china.service.LinkService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsSourceController {

    @Autowired
    private GoodsSourceService goodsSourceService;
    @Autowired
    private CarSourceService carSourceService;
    @Autowired
    private LinkService linkService;

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;


    @PreAuthorize("hasRole('BUSINESS_GOODS')")
    @ApiOperation(value = "查询所有货源",notes = "该业务可查询出所有货源信息")
    @GetMapping("/query")
    public String query(Model model,
                        @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                        @RequestParam(defaultValue="5",value="pageSize")Integer pageSize,
                        @RequestParam(value = "key",required = false)String key,
                        @RequestHeader("authorization") String authorization){
        System.out.println(authorization);
        List<GoodsSource> goods = goodsSourceService.queryAll(pageNum,pageSize,key);
        PageInfo<GoodsSource> pageInfo = new PageInfo<GoodsSource>(goods,pageSize);
        if(goods != null){
            model.addAttribute("goods",goods);
            model.addAttribute("pageInfo",pageInfo);
            return "goodslist";
        }else{
            return null;
        }
    }

    @ApiOperation(value = "查看货源详细内容")
    @ApiImplicitParam(name = "id",value = "货源ID",required = true,dataType = "Integer")
    @GetMapping("/watch/{id}")
    public String watch(@PathVariable("id")Integer id,Model model){

        GoodsSource goodsSource = goodsSourceService.queryGoodsSource(id);
        model.addAttribute("goods",goodsSource);
        return "watchGoods";
    }


    @ApiOperation(value = "添加货源")
    @ApiImplicitParam(name = "goodsSource",value = "货源",required = true,dataType ="GoodsSource")
    @PostMapping("/setGoods")
    public String giveGoods(GoodsSource goodsSource){
            int row = goodsSourceService.insert(goodsSource);
            if(row > 0){
                return "redirect:/api/source/goods/query";
            }
                return null;
    }

    @ApiOperation(value = "删除货源")
    @ApiImplicitParam(name = "id",value = "货源ID",required = true,dataType = "Integer")
    @GetMapping("/delete/{id}")
    public String deleteGoods(@PathVariable("id")Integer id){
        goodsSourceService.deleteById(id);
        return "redirect:/api/source/goods/query";
    }

    @ApiOperation(value = "回显货源数据")
    @GetMapping("/add")
    public String add(Model model){
        List<Province> pros = linkService.getProvince();
        List<Car> cars = goodsSourceService.getCars();
        List<Unit> units = goodsSourceService.getUnits();
        List<Goods> goods = goodsSourceService.getGoods();
        List<Transport> transports = goodsSourceService.getTransports();
        List<GoodsType> goodsTypes = goodsTypeMapper.selectAll();
        model.addAttribute("gcars",cars);
        model.addAttribute("gunits",units);
        model.addAttribute("goodsIds",goods);//种类
        model.addAttribute("gtransports",transports);
        model.addAttribute("gpros",pros);
        model.addAttribute("goodsTypes",goodsTypes);//类别
        return "showAddGoods";
    }


    @ApiIgnore
    @ResponseBody
    @PostMapping("/getCity")
    public List<City> getCity(@RequestParam("id") Integer id){
        List<City> citys = linkService.getCity(id);
        return citys;

    }

    @ApiIgnore
    @ResponseBody
    @PostMapping("/getDistrict")
    public List<District> getDistrict(@RequestParam("id") Integer id, Model model){
        List<District> dists = linkService.getDistrict(id);
        //System.out.println(dists);
        return dists;

    }


    @ApiOperation(value = "修改之前数据回显")
    @ApiImplicitParam(name = "id",value = "货源ID",required = true,dataType = "Integer")
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") Integer id,Model model){
        GoodsSource currGoodsSource = goodsSourceService.queryGoodsSource(id);
       // System.out.println(carSource);

        /**
         * 出发地
         */
        List<Province> startPros = carSourceService.queryProvinces();
        model.addAttribute("startPros",startPros);//省集合

        List<City> startCitys = carSourceService.queryCitys();//市集合
        model.addAttribute("startCitys",startCitys);

        List<District> startDistricts = carSourceService.queryDistricts();//区集合
        model.addAttribute("startDistricts",startDistricts);

        /**
         * 目的地
         */
        List<Province> endPros = carSourceService.queryProvinces();
        model.addAttribute("endPros",endPros);//省集合

        List<City> endCitys = carSourceService.queryCitys();//市集合
        model.addAttribute("endCitys",endCitys);

        List<District> endDistricts = carSourceService.queryDistricts();//区集合
        model.addAttribute("endDistricts",endDistricts);

        //车辆类型
        List<Car> cars = carSourceService.getCars();
        model.addAttribute("cars",cars);

        //单位类型
        List<Unit> units = carSourceService.getUnits();

        model.addAttribute("units",units);

        //货物种类
        List<Goods> goods = carSourceService.getGoods();
        model.addAttribute("goodIds",goods);

        //货物类别
        List<GoodsType> goodsTypes = goodsSourceService.getGoodsType();
        model.addAttribute("goodsTypes",goods);

        //运输类型
        List<Transport> transports = carSourceService.getTransports();
        model.addAttribute("transports",transports);

        model.addAttribute("currGoodsSource",currGoodsSource);
        return "updateGoods";
    }

    @ApiOperation(value = "更新货源")
    @ApiImplicitParam(name = "goodsSource",value = "货源",required = true,dataType = "GoodsSource")
    @PostMapping("/updateGoods")
    public String updateGoods(GoodsSource goodsSource){

        int row = goodsSourceService.update(goodsSource);

        if(row > 0){
            return "redirect:/api/source/goods/query";
        }
        return null;
    }

}
