package io.datajek.tennis_player_rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository repo;


    public List<Player> getAllPlayers() {
        //call repository method
        return this.repo.findAll();
    }

    public Player getPlayer(int id) {
        //find and return the player
        Optional<Player> tempPlayer = repo.findById(id);

        Player p = null;

        //if the Optional has a value, assign it to p
        if(tempPlayer.isPresent())
            p = tempPlayer.get();
            //if value is not found, throw a runtime exception
        else
            throw new PlayerNotFoundException("Player with id "+ id + " not found.");

        return  p;
    }

    public Player addPlayer(Player p) {
        return repo.save(p);
    }

    public Player updatePlayer(int id, Player p) {

        Optional<Player> tempPlayer = repo.findById(id);

        if(tempPlayer.isEmpty())
            throw new PlayerNotFoundException("Player with id "+ id +" not found");

        p.setId(id);
        return repo.save(p);
    }

    public Player patch( int id, Map<String, Object> playerPatch) {

        Optional<Player> player = repo.findById(id);
        if(player.isPresent()) {
            playerPatch.forEach( (key, value) -> {
                Field field = ReflectionUtils.findField(Player.class, key);
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, player.get(), value);
            });
        }else
            throw new PlayerNotFoundException("Player with id {"+ id +"} not found");

        return repo.save(player.get());
    }

    //update a single field
    @Transactional
    public void updateTitles(int id, int titles) {
        Optional<Player> tempPlayer = repo.findById(id);

        if(tempPlayer.isEmpty())
            throw new PlayerNotFoundException("Player with id {"+ id +"} not found");

        repo.updateTitles(id, titles);
    }

    public String deletePlayer(int id) {
        Optional<Player> tempPlayer = repo.findById(id);

        if(tempPlayer.isEmpty()) {
            throw new PlayerNotFoundException("Player with id "+ id + " not found.");
        }

        repo.delete(tempPlayer.get());
        return "Player with id "+ id +" deleted";
    }
}
