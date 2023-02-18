package com.example.dogsitterproject.calback;

import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;

public interface CallBack_AddToFav {
    void addDogToFav(Dog dog);

    void addDogSitterToFav(DogSitter dogSitter);
}
