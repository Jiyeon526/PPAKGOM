package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ConferenceCategoryRegisterPostReq")
public class ConferenceCategoryRegisterPostReq {
    @ApiModelProperty(name="category ID", example="1")
    String id;
    @ApiModelProperty(name="category name", example="업무")
    String name;

}
