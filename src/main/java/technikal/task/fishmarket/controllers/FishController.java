package technikal.task.fishmarket.controllers;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import technikal.task.fishmarket.models.Fish;
import technikal.task.fishmarket.models.FishDto;
import technikal.task.fishmarket.models.FishImage;
import technikal.task.fishmarket.services.FishRepository;

@Controller
@RequestMapping("/fish")
public class FishController {
	
	@Autowired
	private FishRepository repo;
	
	@GetMapping({"", "/"})
	public String showFishList(Model model) {
		List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("fishlist", fishlist);
		return "index";
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		FishDto fishDto = new FishDto();
		model.addAttribute("fishDto", fishDto);
		return "createFish";
	}
	
	@GetMapping("/delete")
	public String deleteFish(@RequestParam int id) {
		try {
			Optional<Fish> optionalFish = repo.findById(id);

			if (optionalFish.isPresent()) {
				Fish fish = optionalFish.get();

				for(FishImage fishImage : fish.getFishImages()) {
					Path imagePath = Paths.get("public/images/" + fishImage.getImageFileName());
					Files.delete(imagePath);
				}

				repo.delete(fish);
			}
		} catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		return "redirect:/fish";
	}
	
	@PostMapping("/create")
	public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {
		if(result.hasErrors()) {
			return "createFish";
		}

		Fish fish = new Fish();

		fish.setCatchDate(new Date());
		fish.setName(fishDto.getName());
		fish.setPrice(fishDto.getPrice());

		List<FishImage> fishImages = new ArrayList<>();

		for (MultipartFile multipartFile: fishDto.getImageFiles()) {
			String storageFileName = (new Date()).getTime() + "_" + multipartFile.getOriginalFilename();

			fishImages.add(new FishImage(null, fish, storageFileName));

			try {
				String uploadDir = "public/images/";
				Path uploadPath = Paths.get(uploadDir);

				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				try(InputStream inputStream = multipartFile.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
		}

		fish.setFishImages(fishImages);

		repo.save(fish);

		return "redirect:/fish";
	}

}
