package technikal.task.fishmarket.models;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import technikal.task.fishmarket.validators.constraints.FishImageFiles;

import java.util.List;

public class FishDto {
	

	@NotEmpty(message = "потрібна назва рибки")
	private String name;
	@Min(0)
	private double price;
	@Size(min = 1, max = 3, message = "не менше однієї і не більше трьох рибок")
	@FishImageFiles
	private List<MultipartFile> imageFiles;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public List<MultipartFile> getImageFiles() {
		return this.imageFiles;
	}
	public void setImageFiles(List<MultipartFile> imageFiles) {
		this.imageFiles = imageFiles;
	}

}
