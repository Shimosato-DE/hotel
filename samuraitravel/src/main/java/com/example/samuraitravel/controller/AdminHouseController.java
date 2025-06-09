package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;


@Controller
@RequestMapping("/admin/houses") //クラスにRequestMappingをつけ、ルートパスの基準値を設定する
public class AdminHouseController {
	
	@Autowired
	private HouseRepository houseRepository;//コンストラクタインジェクションを行う(ここではAutowiredアノテーションにより省略)
	
//	public AdminHouseController(HouseRepository houseRepository) {
//		this.houseRepository = houseRepository;
//	}
	
	@GetMapping
	//public String index(Model model, Pageable pageable) { //Pageable型引数を指定することで、Pageableオブジェクトを生成し、メソッド内で利用可能となる。
	public String index(Model model, @PageableDefault(page=0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		//List<House> houses = houseRepository.findAll(); //HouseRepositoryインターフェイスのfindByAll()メソッドで全てぼ民宿データを取得する
		Page<House> housePages = houseRepository.findAll(pageable);
		
		//model.addAttribute("houses", houses); //viewへ渡す
		model.addAttribute("housePage", housePages);
		
		return "admin/houses/index";
	}
}
