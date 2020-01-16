package com.syc.china.web;


import com.github.pagehelper.PageInfo;
import com.syc.china.pojo.*;
import com.syc.china.service.CarSourceService;
import com.syc.china.service.LinkService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@Controller
@RequestMapping("/cars")
public class CarSourceController {

    @Autowired
    private CarSourceService carSourceService;

    @Autowired
    private LinkService linkService;



    @ApiOperation(value = "展示车源信息",notes = "展示所有的车源")
    @GetMapping("/query")
    public String query(Model model,
                        @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                        @RequestParam(defaultValue="5",value="pageSize")Integer pageSize,
                        @RequestParam(value = "key",required = false)String key){


        List<CarSource> cars = carSourceService.queryAll(pageNum,pageSize,key);
        PageInfo<CarSource> pageInfo = new PageInfo<CarSource>(cars,pageSize);
        if(cars != null){
            model.addAttribute("cars",cars);
            model.addAttribute("pageInfo",pageInfo);
            return "carlist";
        }else{
            return  "dashboard";
        }
    }


/*@RequestMapping("page")
public ResponseEntity<PageResult> page(
        @RequestParam(value = "page",defaultValue = "1")Integer page,
        @RequestParam(value = "rows",defaultValue = "5") Integer rows,
        @RequestParam(value = "key",required = false)String key){
    PageResult<CarSource> result=this.carSourceService.queryCarsourcesPage(page,rows,key);
    return  ResponseEntity.ok(result);
}*/
    @ApiOperation(value = "添加页面的回显",notes = "该业务可回显下拉框中的数据")
    @GetMapping("/add")
    public String add(Model model){
        List<Province> pros = linkService.getProvince();
        List<Car> cars = carSourceService.getCars();
        List<Unit> units = carSourceService.getUnits();
        List<Goods> goods = carSourceService.getGoods();
        List<Transport> transports = carSourceService.getTransports();
        model.addAttribute("cars",cars);
        model.addAttribute("units",units);
        model.addAttribute("goods",goods);
        model.addAttribute("transports",transports);
        model.addAttribute("pros",pros);
        return "showAdd";
    }


    @ApiOperation(value = "获取城市集合",notes ="获得所有的城市名" )
    @ApiImplicitParam(name = "id",value = "省ID",required = true,dataType ="Integer")
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
        return dists;
    }


    @ApiOperation(value = "修改之前回显")
    @ApiImplicitParam(name = "id",value = "车源ID",dataType = "Integer",required = true)
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") Integer id,Model model){
        CarSource carSource = carSourceService.queryCarSource(id);
        System.out.println(carSource);

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

        //可载种类
        List<Goods> goods = carSourceService.getGoods();
        model.addAttribute("goods",goods);


        //运输类型
        List<Transport> transports = carSourceService.getTransports();
        model.addAttribute("transports",transports);

        //当前对象
        model.addAttribute("currCarSource",carSource);
        return "update";
    }


    @ApiOperation(value ="发布车源")
    @ApiImplicitParam(name = "carSource",value = "车源",required = true,dataType = "CarSource")
    @PostMapping("/setCar")
    public String giveCar( CarSource carSource){
        int rows = carSourceService.insert(carSource);
        if(rows > 0){
            return "redirect:/api/source/cars/query";
        }
        return  null;
    }

    @ApiOperation(value = "修改车源信息")
    @ApiImplicitParam(name = "carSource",value = "车源",required = true,dataType = "CarSource")
    @PostMapping("/updateCar")
    public String updateCar(CarSource carSource){
        System.out.println(carSource);
        int rows = carSourceService.update(carSource);
        if(rows > 0){
            return "redirect:/api/source/cars/query";
        }
        return null;
    }

    @ApiOperation(value = "删除车源")
    @ApiImplicitParam(name = "id",value = "车源ID",required = true,dataType = "Integer")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Integer id,Model model){
        carSourceService.deleteById(id);
        return "redirect:/api/source/cars/query";
    }

    @ApiOperation("查看车源详情")
    @ApiImplicitParam(name = "id",value = "车源ID",required = true,dataType = "CarSource")
    @GetMapping("/watch/{id}")
    public String watch(@PathVariable("id")Integer id,Model model){

            CarSource CarSource = carSourceService.queryCarSource(id);
            model.addAttribute("cars",CarSource);
        return "watch";
    }

    /*按照地区搜索*/







}
