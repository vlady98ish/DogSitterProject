package com.example.dogsitterproject.calback;

import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;

public interface CallBack_RemoveFromFav {
    void removeDogFromFav(Dog item);

    void removeDogSitterFromFav(DogSitter dogSitter);
}
