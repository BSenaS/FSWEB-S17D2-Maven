package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.workintech.s17d2.model.Experience.JUNIOR;
import static com.workintech.s17d2.model.Experience.MID;

@RestController
public class DeveloperController {
    public Map<Integer,Developer> developers;
    public Taxable taxable;


    @Autowired
    public DeveloperController(Taxable taxable){
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        this.developers = new HashMap<>();
        Developer developer = new Developer(1, "Pepega Developer", 25000.0, Experience.JUNIOR);
        developers.put(developer.getId(), developer);
    }

    //[GET]/workintech/developers => tüm developers mapinin value değerlerini List olarak döner.
    @GetMapping("/developers")
    public List<Developer> getAllDevelopers(){
        return new ArrayList<>(this.developers.values());
    }

    //[GET]/workintech/developers/{id} => ilgili id deki developer mapte varsa value değerini döner.
    @GetMapping("/developers/{id}")
    public Developer getDeveloperById(@PathVariable int id){
        return developers.get(id);
    }

    //[POST]/workintech/developers => id, name, salary ve experience değerlerini alır,
    // experience tipine bakarak uygun developer objesini oluşturup developers mapine ekler.
    // JuniorDeveloper için salary bilgisinden salarygetSimpleTaxRate() değerini düşmelisiniz.
    // Aynı şekilde MidDeveloper için salarygetMiddleTaxRate(),
    // SeniorDeveloper için salary*getUpperTaxRate() değerlerini salary bilgisinden düşmelisiniz.

    @PostMapping("/developers")
    public void addDeveloper(@RequestBody Developer developer){
        double taxRate;
        double salary = developer.getSalary();
        if(developer.getExperience() == JUNIOR){
            taxRate = taxable.getSimpleTaxRate();
            developer.setSalary(salary - (salary * (taxRate / 100)));
        }else if(developer.getExperience() == MID){
            taxRate = taxable.getMiddleTaxRate();
            developer.setSalary(salary - (salary * (taxRate / 100)));
        }else{
            taxRate = taxable.getUpperTaxRate();
            developer.setSalary(salary - (salary * (taxRate / 100)));
        }
        this.developers.put(developer.getId(),developer);
    }

    //[PUT]/workintech/developers/{id} => İlgili id deki map değerini Request Body içerisinden aldığı değer ile günceller.
    @PutMapping("/developers/{id}")
    public Developer update(@PathVariable int id,@RequestBody Developer newDeveloper){
        this.developers.replace(id, newDeveloper);
        return this.developers.get(id);
    }

    //[DELETE]/workintech/developers/{id} => İlgili id değerini mapten siler.
    @DeleteMapping("/developers/{id}")
    public void delete(@PathVariable int id){
        this.developers.remove(id);
    }


}
