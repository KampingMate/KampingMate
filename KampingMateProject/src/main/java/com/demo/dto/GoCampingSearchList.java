package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoCampingSearchList {

	private String doNm;
	private String sigunguNm;
	private String facltNm;
	private String intro;
	private String addr1;
	private String tel;
	private String firstImageUrl;
	private String sbrsCl;
	private int contentId;
}
