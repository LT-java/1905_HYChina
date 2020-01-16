package com.syc.china.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_goods_source")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSource {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    private Integer provinceId;
    @JsonIgnore
    private Integer cityId;
    @JsonIgnore
    private Integer districtId;
    @Transient
    private String startName;

    @JsonIgnore
    private Integer provinceIdEnd;
    @JsonIgnore
    private Integer cityIdEnd;
    @JsonIgnore
    private Integer districtIdEnd;
    @Transient
    private String endName;

    private Date startTime;

    private Double weight;

    private String goodsName;

    @JsonIgnore
    private Integer goodsId;
    @Transient
    private String goodsIdName;


    @JsonIgnore
    private Integer goodsTypeId;
    @Transient
    private String goodsTypeName;

    @JsonIgnore
    private Integer carId;
    @Transient
    private String carName;

    @JsonIgnore
    private Integer transportId;
    @Transient
    private String transportName;

    private Double price;

    @JsonIgnore
    private Integer unitId;
    @Transient
    private String unitName;


    private String ownerName;
    private String phone;
    private String email;
    private String qq;
    private String remarks;



}
