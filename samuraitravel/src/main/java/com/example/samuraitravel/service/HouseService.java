package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;

@Service
public class HouseService {
	private final HouseRepository houseRepository;
	
	public HouseService(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	// 新規登録用

	@Transactional // メソッドをトランザクション化することができる
	public void create(HouseRegisterForm houseRegisterForm) {

		House house = new House(); // Houseエンティティのインスタンス生成
		MultipartFile imageFile = houseRegisterForm.getImageFile(); // getを使用して、houseRegisterFormのimageFileを取得し、ここでのimageFile変数に格納

		if (!imageFile.isEmpty()) { // imgefileが空かどうか判定。↓空の場合、

			String imageName = imageFile.getOriginalFilename();// imageFileから元のファイル名を取得
			String hashedImageName = generateNewFileName(imageName);// generateNewFileName()メソッドを呼び出し、hashedImageNameへ格納

			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);

			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}

		// houseRegisterFormから各項目をgetし、houseエンティティへset
		house.setName(houseRegisterForm.getName());
		house.setDescription(houseRegisterForm.getDescription());
		house.setPrice(houseRegisterForm.getPrice());
		house.setCapacity(houseRegisterForm.getCapacity());
		house.setPostalCode(houseRegisterForm.getPostalCode());
		house.setAddress(houseRegisterForm.getAddress());
		house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

		houseRepository.save(house);// Repository層呼び出しDBへ登録処理

	}

	// 更新用

	@Transactional
	public void update(HouseEditForm houseEditForm) {

		House house = houseRepository.getReferenceById(houseEditForm.getId());
		MultipartFile imageFile = houseEditForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);

			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);

			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}

		house.setName(houseEditForm.getName());
		house.setDescription(houseEditForm.getDescription());
		house.setPrice(houseEditForm.getPrice());
		house.setCapacity(houseEditForm.getCapacity());
		house.setPostalCode(houseEditForm.getPostalCode());
		house.setAddress(houseEditForm.getAddress());
		house.setPhoneNumber(houseEditForm.getPhoneNumber());

		houseRepository.save(house);
	}

	// UUIDを使って生成したファイル名を設定
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}

		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}

	// 画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
