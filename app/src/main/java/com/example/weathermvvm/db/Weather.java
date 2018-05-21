package com.example.weathermvvm.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Store weather details
 */

@Entity
public class Weather {

    @Id private Long id;

    private String name;
    private String country;

    private Double temperature;
    private Double pressure;

    private Integer humidity;
    private Double windSpeed;
    private String icon;
    @Generated(hash = 410050412)
    public Weather(Long id, String name, String country, Double temperature,
            Double pressure, Integer humidity, Double windSpeed, String icon) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.icon = icon;
    }
    @Generated(hash = 556711069)
    public Weather() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public Double getTemperature() {
        return this.temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public Double getPressure() {
        return this.pressure;
    }
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }
    public Integer getHumidity() {
        return this.humidity;
    }
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
    public Double getWindSpeed() {
        return this.windSpeed;
    }
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

}
