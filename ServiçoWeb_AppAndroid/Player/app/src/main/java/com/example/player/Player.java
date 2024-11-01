package com.example.player;

import org.json.JSONObject;

public class Player {
    private int id;
    private String name;
    private String nationality;
    private String birthDate;
    private int titles;

    public Player(int id, String name, String nationality, String birthDate, int titles) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.titles = titles;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("nationality", nationality);
            jsonObject.put("birthDate", birthDate);
            jsonObject.put("titles", titles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", birthDate=" + birthDate +
                ", titles=" + titles +
                '}';
    }
}

