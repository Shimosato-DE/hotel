package com.example.samuraitravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;

//②宿泊施設用DB接続Repository
public interface HouseRepository extends JpaRepository<House, Integer>{ //エンティティのクラス型と、主キーのデータ型を指定

}
