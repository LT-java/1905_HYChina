package com.syc.china.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_car_source")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSource {
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
    @JsonIgnore
    private Integer goodsId;
    @Transient
    private String goodsName;

    @JsonIgnore
    private Integer carId;
    @Transient
    private String carName;
    @JsonIgnore
    private Integer transportId;
    @Transient
    private String transportName;
    @JsonIgnore
    private Integer unitId;
    @Transient
    private String unitName;

    private String ownerName;
    private String phone;
    private String email;
    private String qq;
    private String remarks;
    private Double price;
    private Integer quantity;

}
