package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;

//②宿泊施設用DB接続Repository
public interface HouseRepository extends JpaRepository<House, Integer>{ //エンティティのクラス型と、主キーのデータ型を指定
	
	public Page<House> findByNameLike(String keyword, Pageable pageable);
	
	public Page<House> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);
		
	public Page<House> findByAddressLike(String area, Pageable pageable);
}
