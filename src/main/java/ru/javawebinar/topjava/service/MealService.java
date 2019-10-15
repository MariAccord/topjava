package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final UserMealRepository repository;

    @Autowired
    public MealService(UserMealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }
    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }
    public void update (Meal meal, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
    public Collection<Meal> getBetween(LocalDateTime startTime, LocalDateTime endTime, int userId) {
        return repository.getBetween(startTime, endTime, userId);
    }
}