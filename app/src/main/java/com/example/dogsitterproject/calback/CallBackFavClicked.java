package com.example.dogsitterproject.calback;

import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;


public interface CallBackFavClicked {
    void favClicked(Dog dog);

    void favClicked(DogSitter dogSitter);

    void removeFromFav(Dog item);

    void removeFromFav(DogSitter dogSitter);
}
