package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

//①DBデータ受渡用エンティティ
@Entity //エンティティとして機能
@Table(name="houses") //マッピングされるテーブル名を指定
@Data //Getter,Setter等の自動生成
public class House {
	
	@Id //主キーに指定
	@GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENTを指定したカラムを利用して値を生成
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "image_name")
	private String imageName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@Column(name = "postal_code")
	private String postalCode;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "created_at", insertable = false, updatable = false) //カラムの値を挿入できるか、更新できるか。trueの場合、自身で指定する。
	private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updateAt;
	
	

}
