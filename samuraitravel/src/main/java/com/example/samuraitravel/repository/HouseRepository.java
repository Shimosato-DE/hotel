package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;

//②宿泊施設用DB接続Repository
public interface HouseRepository extends JpaRepository<House, Integer>{ //エンティティのクラス型と、主キーのデータ型を指定
	
	
	public Page<House> findByNameLike(String keyword, Pageable pageable);
	
	public Page<House> findByAddressLike(String area, Pageable pageable);
	
	public Page<House> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);
	
	public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	
	public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);
	
	public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
	
	public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
	
	public Page<House> findByPriceLessThanEqual(Integer price, Pageable pageable);
	
	public Page<House> findBypriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	
	public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);

	public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
	public Page<House> findAllByOrderByPriceAsc(Pageable pageable);
	
	public List<House> findTop10ByOrderByCreatedAtDesc();
	
}
