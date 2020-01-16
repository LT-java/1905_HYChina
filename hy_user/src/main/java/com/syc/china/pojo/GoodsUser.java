package com.syc.china.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_goods_user")
@Data
public class GoodsUser {

    @Id
    private Integer userId;

    private String company;

    private String companyPersonName; //企业联系人姓名

    private String companyPersonPhone; //企业联系人电话

    private Integer goodsId; //主营商品种类

    private String certificates; //资质营业照

    private String email;

    private String wechat;

    private String QQ;


}
