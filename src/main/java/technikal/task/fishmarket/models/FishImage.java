package technikal.task.fishmarket.models;

import jakarta.persistence.*;

@Entity
@Table(name = "fish_images")
public class FishImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fish_id", nullable = false)
    private Fish fish;

    private String imageFileName;

    public FishImage() {
        //
    }

    public FishImage(Integer id, Fish fish, String imageFileName) {
        this.id = id;
        this.fish = fish;
        this.imageFileName = imageFileName;
    }

    public Integer getId() {
        return this.id;
    }

    public Fish getFish() {
        return this.fish;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
