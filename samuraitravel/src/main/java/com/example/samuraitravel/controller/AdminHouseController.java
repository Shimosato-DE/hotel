package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;


@Controller
@RequestMapping("/admin/houses") //クラスにRequestMappingをつけ、ルートパスの基準値を設定する
public class AdminHouseController {
	
	@Autowired
	private HouseRepository houseRepository;//コンストラクタインジェクションを行う
	
//	public AdminHouseController(HouseRepository houseRepository) {
//		this.houseRepository = houseRepository;
//	}
	
//	@GetMapping
//	public String index(Model model, Pageable pageable) { //Pageable型引数を指定することで、Pageableオブジェクトを生成し、メソッド内で利用可能となる。
//		List<House> houses = houseRepository.findAll(); //HouseRepositoryインターフェイスのfindByAll()メソッドで全てぼ民宿データを取得する
//		model.addAttribute("houses", houses); //viewへ渡す
//		return "admin/houses/index";
//	}
	
	//GETリクエスト(/admin/houses)を受取、民宿一覧及び検索、登録画面を返す
	
	@GetMapping
	public String index(Model model, @PageableDefault(page=0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		
		Page<House> housePage = houseRepository.findAll(pageable);//ページの全件取得
		
		if(keyword != null && !keyword.isEmpty()) { //keywordパラメータが存在するかどうか
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable); //存在すれば検索/取得してhousePageに格納
		}else {
			housePage = houseRepository.findAll(pageable); //無ければ全件取得
		}
		
		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword",keyword);
		
		return "admin/houses/index";
	}
	
	
	
	
	//一覧ページから/{id}を受取、各民宿の詳細ページを返す
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) { //@PathVariable:name属性にバインドさせたいURLの{}内の文字列を指定。
		House house = houseRepository.getReferenceById(id); //getReferenceById(id):引数idと一致するデータを取得する
		model.addAttribute("house", house);
		
		return "admin/houses/show";
	}
	
	@GetMapping("/register")
	public String register(@ModelAttribute HouseRegisterForm form) {
		return "admin/houses/register";
	}
	
}
