package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.workintech.s17d2.model.Experience.JUNIOR;
import static com.workintech.s17d2.model.Experience.MID;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer,Developer> developers;
    private Taxable taxable;

    //DeveloperController sınıfı içerisinde bir adet constructor tanımlanmalı Taxable interface Dependency Injection yöntemiyle çağırılmalı. DeveloperTax sınıfını çağırmalı.

    @Autowired
    public DeveloperController(DeveloperTax taxable){
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        this.developers = new HashMap<>();
        developers.put(1,new Developer(1,"Batu",2500.0, JUNIOR));
    }

    //[GET]/workintech/developers => tüm developers mapinin value değerlerini List olarak döner.
    @GetMapping
    public List<Developer> getAllDevelopers(){
        return new ArrayList<>(this.developers.values());
    }

    //[GET]/workintech/developers/{id} => ilgili id deki developer mapte varsa value değerini döner.
    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id){
        return developers.get(id);
    }

    //[POST]/workintech/developers => id, name, salary ve experience değerlerini alır,
    // experience tipine bakarak uygun developer objesini oluşturup developers mapine ekler.
    // JuniorDeveloper için salary bilgisinden salarygetSimpleTaxRate() değerini düşmelisiniz.
    // Aynı şekilde MidDeveloper için salarygetMiddleTaxRate(),
    // SeniorDeveloper için salary*getUpperTaxRate() değerlerini salary bilgisinden düşmelisiniz.

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    @PutMapping("/{id}")
    public Developer update(@PathVariable int id,@RequestBody Developer newDeveloper){
        this.developers.replace(id, newDeveloper);
        return this.developers.get(id);
    }

    //[DELETE]/workintech/developers/{id} => İlgili id değerini mapten siler.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        this.developers.remove(id);
    }


}
